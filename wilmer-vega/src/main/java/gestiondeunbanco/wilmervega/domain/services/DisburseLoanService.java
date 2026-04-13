package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

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

    public Loan disburse(Long loanId, Long disbursementAccountId, Long analystUserId, String analystRole) {
        // 1. Find loan
        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + loanId));

        // 2. Validate status is APPROVED
        if (loan.getLoanStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException(
                    "Loan cannot be disbursed. Current status: " + loan.getLoanStatus()
                            + ". Expected: APPROVED");
        }

        // 3. Validate approvedAmount > 0
        if (loan.getApprovedAmount() == null || loan.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Approved amount must be greater than zero for disbursement.");
        }

        // 4. Find and validate disbursement account
        BankAccount account = bankAccountPort.findById(disbursementAccountId)
                .orElseThrow(() -> new NotFoundException("Disbursement account not found with ID: " + disbursementAccountId));

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Disbursement account is not ACTIVE. Status: " + account.getAccountStatus());
        }

        BigDecimal balanceBefore = account.getCurrentBalance();

        // 5. Increase account balance
        account.setCurrentBalance(balanceBefore.add(loan.getApprovedAmount()));
        bankAccountPort.save(account);

        // 6. Update loan
        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDate.now());
        loan.setDisbursementAccount(account);
        Loan savedLoan = loanPort.save(loan);

        // 7. Register in audit log (NoSQL)
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
    }
}
