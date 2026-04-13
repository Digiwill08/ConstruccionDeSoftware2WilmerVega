package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Approves a loan that is UNDER_REVIEW.
 * Only an INTERNAL_ANALYST should invoke this service.
 * Transitions: UNDER_REVIEW -> APPROVED
 */
public class ApproveLoanService {

    private final LoanPort loanPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public ApproveLoanService(LoanPort loanPort, AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    public Loan approve(Long loanId, Long analystUserId, String analystRole) {
        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + loanId));

        if (loan.getLoanStatus() != LoanStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Loan cannot be approved. Current status: " + loan.getLoanStatus()
                            + ". Expected: UNDER_REVIEW");
        }

        if (loan.getApprovedAmount() == null || loan.getApprovedAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Approved amount must be greater than zero to approve a loan.");
        }

        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());
        loan.setApprovedByUserId(analystUserId);

        Loan savedLoan = loanPort.save(loan);

        // Register in audit log (NoSQL)
        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.LOAN_APPROVAL);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(analystUserId);
        log.setUserRole(analystRole);
        log.setAffectedProductId(String.valueOf(loanId));

        Map<String, Object> details = new HashMap<>();
        details.put("loanId", loanId);
        details.put("analystUserId", analystUserId);
        details.put("previousStatus", "UNDER_REVIEW");
        details.put("newStatus", "APPROVED");
        details.put("approvedAmount", loan.getApprovedAmount());
        details.put("approvalDate", LocalDate.now().toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);

        return savedLoan;
    }
}
