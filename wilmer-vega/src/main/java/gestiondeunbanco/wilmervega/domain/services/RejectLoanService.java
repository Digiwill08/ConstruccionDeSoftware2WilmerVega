package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RejectLoanService {

    private final LoanPort loanPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public RejectLoanService(LoanPort loanPort, AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Loan reject(Long loanId, Long analystUserId, String analystRole, String reason) {
        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Prestamo no encontrado con ID: " + loanId));

        if (loan.getLoanStatus() != LoanStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "El prestamo no puede ser rechazado. Estado actual: " + loan.getLoanStatus()
                    + ". Estado requerido: UNDER_REVIEW");
        }

        loan.setLoanStatus(LoanStatus.REJECTED);
        Loan savedLoan = loanPort.save(loan);

        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.LOAN_REJECTION);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(analystUserId);
        log.setUserRole(analystRole);
        log.setAffectedProductId(String.valueOf(loanId));

        Map<String, Object> details = new HashMap<>();
        details.put("loanId", loanId);
        details.put("analystUserId", analystUserId);
        details.put("analystRole", analystRole);
        details.put("estadoAnterior", "UNDER_REVIEW");
        details.put("estadoNuevo", "REJECTED");
        details.put("fechaRechazo", LocalDate.now().toString());
        details.put("motivo", reason != null ? reason : "Sin motivo especificado");
        log.setDetails(details);

        auditLogMongoPort.save(log);
        return savedLoan;
    }
}
