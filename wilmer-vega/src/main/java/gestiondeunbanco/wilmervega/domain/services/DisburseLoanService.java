package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Disburses an APPROVED loan to the client's bank account.
 * Transitions: APPROVED -> DISBURSED
 * Business rules:
 *   - Loan must be APPROVED
 *   - disbursementAccount must be defined and ACTIVE
 *   - approvedAmount must be > 0
 *   - Increases the balance of the destination account
 */
public class DisburseLoanService {

    private final LoanPort loanPort;
    private final BankAccountPort bankAccountPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public DisburseLoanService(LoanPort loanPort, BankAccountPort bankAccountPort,
                               AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Loan disburse(Long loanId, Long disbursementAccountId, Long analystUserId, String analystRole) {
        try {
            validateAnalystRole(analystRole);

            Loan loan = loanPort.findById(loanId)
                    .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + loanId));

            if (loan.getLoanStatus() != LoanStatus.APPROVED) {
                throw new IllegalStateException(
                        "Loan cannot be disbursed. Current status: " + loan.getLoanStatus()
                                + ". Expected: APPROVED");
            }

            if (loan.getApprovedAmount() == null || loan.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Approved amount must be greater than zero for disbursement.");
            }

            BankAccount account = bankAccountPort.findById(disbursementAccountId)
                    .orElseThrow(() -> new NotFoundException("Disbursement account not found with ID: " + disbursementAccountId));

            if (account.getAccountStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException("Disbursement account is not ACTIVE. Status: " + account.getAccountStatus());
            }

            if (loan.getClientApplicant() == null || loan.getClientApplicant().getId() == null
                    || account.getHolder() == null || account.getHolder().getId() == null
                    || !loan.getClientApplicant().getId().equals(account.getHolder().getId())) {
                throw new IllegalStateException("Disbursement account must belong to the loan applicant");
            }

            BigDecimal balanceBefore = account.getCurrentBalance();
            account.setCurrentBalance(balanceBefore.add(loan.getApprovedAmount()));
            bankAccountPort.save(account);

            loan.setLoanStatus(LoanStatus.DISBURSED);
            loan.setDisbursementDate(LocalDate.now());
            loan.setDisbursementAccount(account);
            Loan savedLoan = loanPort.save(loan);

            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.LOAN_DISBURSEMENT);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(analystUserId);
            log.setUserRole(analystRole);
            log.setAffectedProductId(String.valueOf(loanId));

            Map<String, Object> details = new HashMap<>();
            details.put("loanId", loanId);
            details.put("analystUserId", analystUserId);
            details.put("previousStatus", "APPROVED");
            details.put("newStatus", "DISBURSED");
            details.put("disbursedAmount", loan.getApprovedAmount());
            details.put("destinationAccount", account.getAccountNumber());
            details.put("balanceBefore", balanceBefore);
            details.put("balanceAfter", account.getCurrentBalance());
            details.put("disbursementDate", LocalDate.now().toString());
            log.setDetails(details);

            auditLogMongoPort.save(log);
            return savedLoan;
        } catch (RuntimeException ex) {
            registerFailure(loanId, analystUserId, analystRole, disbursementAccountId, ex);
            throw ex;
        }
    }

    private void validateAnalystRole(String analystRole) {
        if (analystRole == null || !SystemRole.INTERNAL_ANALYST.name().equals(analystRole)) {
            throw new IllegalStateException("Only INTERNAL_ANALYST can modify loan states");
        }
    }

    private void registerFailure(Long loanId, Long userId, String role, Long accountId, RuntimeException ex) {
        try {
            AuditLog failureLog = new AuditLog();
            failureLog.setOperationType(OperationType.SECURITY_VALIDATION_FAILURE);
            failureLog.setOperationDateTime(LocalDateTime.now());
            failureLog.setUserId(userId);
            failureLog.setUserRole(role);
            failureLog.setAffectedProductId(String.valueOf(loanId));

            Map<String, Object> details = new HashMap<>();
            details.put("operation", "LOAN_DISBURSEMENT");
            details.put("loanId", loanId);
            details.put("disbursementAccountId", accountId);
            details.put("success", false);
            details.put("errorType", ex.getClass().getSimpleName());
            details.put("errorMessage", ex.getMessage());
            failureLog.setDetails(details);

            auditLogMongoPort.save(failureLog);
        } catch (Exception ignored) {
            // Do not block rollback flow if audit persistence is temporarily unavailable.
        }
    }
}
