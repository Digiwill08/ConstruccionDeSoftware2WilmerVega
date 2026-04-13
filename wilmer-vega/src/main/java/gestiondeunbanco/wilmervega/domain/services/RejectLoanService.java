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
 * Rejects a loan that is UNDER_REVIEW.
 * Only an INTERNAL_ANALYST should invoke this service.
 * Transitions: UNDER_REVIEW -> REJECTED
 */
public class RejectLoanService {

    private final LoanPort loanPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public RejectLoanService(LoanPort loanPort, AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    public Loan reject(Long loanId, Long analystUserId, String analystRole, String reason) {
        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + loanId));

        if (loan.getLoanStatus() != LoanStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Loan cannot be rejected. Current status: " + loan.getLoanStatus()
                            + ". Expected: UNDER_REVIEW");
        }

        loan.setLoanStatus(LoanStatus.REJECTED);

        Loan savedLoan = loanPort.save(loan);

        // Register in audit log (NoSQL)
        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.LOAN_REJECTION);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(analystUserId);
        log.setUserRole(analystRole);
        log.setAffectedProductId(String.valueOf(loanId));

        Map<String, Object> details = new HashMap<>();
        details.put("loanId", loanId);
        details.put("analystUserId", analystUserId);
        details.put("previousStatus", "UNDER_REVIEW");
        details.put("newStatus", "REJECTED");
        details.put("rejectionDate", LocalDate.now().toString());
        details.put("reason", reason != null ? reason : "No reason provided");
        log.setDetails(details);

        auditLogMongoPort.save(log);

        return savedLoan;
    }
}
