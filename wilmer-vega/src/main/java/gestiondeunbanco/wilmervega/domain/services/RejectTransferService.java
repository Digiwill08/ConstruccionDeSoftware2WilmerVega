package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

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

    public Transfer reject(Long transferId, Long supervisorUserId, String supervisorRole, String reason) {
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

        // Register in audit log
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
    }
}
