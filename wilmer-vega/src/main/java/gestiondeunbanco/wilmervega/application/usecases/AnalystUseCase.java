package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.services.*;

import java.util.List;

/**
 * Use case for the INTERNAL_ANALYST role.
 * Responsible for: approving/rejecting/disbursing loans, consulting full audit log.
 */
public class AnalystUseCase {

    private final ApproveLoanService approveLoanService;
    private final RejectLoanService rejectLoanService;
    private final DisburseLoanService disburseLoanService;
    private final FindLoan findLoan;
    private final AuditLogMongoPort auditLogMongoPort;

    public AnalystUseCase(ApproveLoanService approveLoanService,
                          RejectLoanService rejectLoanService,
                          DisburseLoanService disburseLoanService,
                          FindLoan findLoan,
                          AuditLogMongoPort auditLogMongoPort) {
        this.approveLoanService = approveLoanService;
        this.rejectLoanService = rejectLoanService;
        this.disburseLoanService = disburseLoanService;
        this.findLoan = findLoan;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    // --- Loan management ---
    public List<Loan> findAllLoans() { return findLoan.findAll(); }

    public Loan findLoanById(Long id) { return findLoan.findById(id); }

    public Loan approveLoan(Long loanId, Long analystUserId, String role) {
        return approveLoanService.approve(loanId, analystUserId, role);
    }

    public Loan rejectLoan(Long loanId, Long analystUserId, String role, String reason) {
        return rejectLoanService.reject(loanId, analystUserId, role, reason);
    }

    public Loan disburseLoan(Long loanId, Long disbursementAccountId, Long analystUserId, String role) {
        return disburseLoanService.disburse(loanId, disbursementAccountId, analystUserId, role);
    }

    // --- Audit log (full access) ---
    public List<AuditLog> findAllAuditLogs() { return auditLogMongoPort.findAll(); }

    public List<AuditLog> findAuditLogsByProduct(String productId) {
        return auditLogMongoPort.findByAffectedProductId(productId);
    }

    public List<AuditLog> findAuditLogsByUser(Long userId) {
        return auditLogMongoPort.findByUserId(userId);
    }
}
