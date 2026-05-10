package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Rejects a high-value transfer that is AWAITING_APPROVAL.
 * Only a COMPANY_SUPERVISOR should invoke this service.
 * Transitions: AWAITING_APPROVAL -> REJECTED
 */
public class RejectTransferService {

    private final TransferPort transferPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public RejectTransferService(TransferPort transferPort, AuditLogMongoPort auditLogMongoPort) {
        this.transferPort = transferPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Transfer reject(Long transferId, Long supervisorUserId, String supervisorRole, String reason) {
        try {
            validateSupervisorRole(supervisorRole);

            Transfer transfer = transferPort.findById(transferId)
                    .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + transferId));

            if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
                throw new IllegalStateException("Transfer cannot be rejected. Current status: "
                        + transfer.getTransferStatus() + ". Expected: AWAITING_APPROVAL");
            }

            transfer.setTransferStatus(TransferStatus.REJECTED);
            transfer.setApprovalDateTime(LocalDateTime.now());
            transfer.setApproverUserId(supervisorUserId);
            Transfer savedTransfer = transferPort.save(transfer);

            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.TRANSFER_REJECTED);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(supervisorUserId);
            log.setUserRole(supervisorRole);
            log.setAffectedProductId(String.valueOf(transferId));

            Map<String, Object> details = new HashMap<>();
            details.put("transferId", transferId);
            details.put("supervisorUserId", supervisorUserId);
            details.put("previousStatus", "AWAITING_APPROVAL");
            details.put("newStatus", "REJECTED");
            details.put("rejectionDateTime", LocalDateTime.now().toString());
            details.put("reason", reason != null ? reason : "No reason provided");
            log.setDetails(details);

            auditLogMongoPort.save(log);
            return savedTransfer;
        } catch (RuntimeException ex) {
            registerFailure(transferId, supervisorUserId, supervisorRole, ex);
            throw ex;
        }
    }

    private void validateSupervisorRole(String supervisorRole) {
        if (supervisorRole == null || !SystemRole.COMPANY_SUPERVISOR.name().equals(supervisorRole)) {
            throw new IllegalStateException("Only COMPANY_SUPERVISOR can approve or reject high-value transfers");
        }
    }

    private void registerFailure(Long transferId, Long supervisorUserId, String supervisorRole, RuntimeException ex) {
        try {
            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.SECURITY_VALIDATION_FAILURE);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(supervisorUserId);
            log.setUserRole(supervisorRole);
            log.setAffectedProductId(String.valueOf(transferId));

            Map<String, Object> details = new HashMap<>();
            details.put("operation", "TRANSFER_REJECTION");
            details.put("transferId", transferId);
            details.put("success", false);
            details.put("errorType", ex.getClass().getSimpleName());
            details.put("errorMessage", ex.getMessage());
            log.setDetails(details);

            auditLogMongoPort.save(log);
        } catch (Exception ignored) {
            // Do not block rollback flow if audit persistence fails.
        }
    }
}
