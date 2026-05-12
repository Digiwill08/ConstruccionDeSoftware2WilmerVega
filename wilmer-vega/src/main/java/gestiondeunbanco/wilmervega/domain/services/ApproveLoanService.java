package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.InvalidApprovedAmountException;
import gestiondeunbanco.wilmervega.domain.exceptions.LoanNotReviewableException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.domain.models.OperationType;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ApproveLoanService {

    private final LoanPort loanPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public ApproveLoanService(LoanPort loanPort, AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Loan approve(Long loanId, Long analystUserId, String analystRole) {
        LocalDateTime operationDateTime = LocalDateTime.now();
        LocalDate operationDate = operationDateTime.toLocalDate();

        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Prestamo no encontrado con ID: " + loanId));

        if (loan.getLoanStatus() != LoanStatus.UNDER_REVIEW) {
            throw new LoanNotReviewableException(
                    "El prestamo no puede ser aprobado. Estado actual: " + loan.getLoanStatus()
                    + ". Estado requerido: UNDER_REVIEW");
        }

        if (loan.getApprovedAmount() == null
                || loan.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidApprovedAmountException("El monto aprobado debe ser mayor a cero para aprobar el prestamo.");
        }

        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(operationDate);
        loan.setApprovedByUserId(analystUserId);
        Loan savedLoan = loanPort.save(loan);

        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.LOAN_APPROVAL);
        log.setOperationDateTime(operationDateTime);
        log.setUserId(analystUserId);
        log.setUserRole(analystRole);
        log.setAffectedProductId(String.valueOf(loanId));

        Map<String, Object> details = new HashMap<>();
        details.put("loanId", loanId);
        details.put("analystUserId", analystUserId);
        details.put("analystRole", analystRole);
        details.put("estadoAnterior", "UNDER_REVIEW");
        details.put("estadoNuevo", "APPROVED");
        details.put("montoAprobado", loan.getApprovedAmount());
        details.put("fechaAprobacion", operationDate.toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);
        return savedLoan;
    }
}
