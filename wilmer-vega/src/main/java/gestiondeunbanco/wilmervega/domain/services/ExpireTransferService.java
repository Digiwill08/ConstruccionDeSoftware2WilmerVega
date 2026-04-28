package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Expires transfers that have been AWAITING_APPROVAL for more than 60 minutes.
 * Called by the TransferExpirationScheduler every minute.
 * Transitions: AWAITING_APPROVAL -> EXPIRED
 */
public class ExpireTransferService {

    private static final long APPROVAL_TIMEOUT_MINUTES = 60;

    private final TransferPort transferPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public ExpireTransferService(TransferPort transferPort, AuditLogMongoPort auditLogMongoPort) {
        this.transferPort = transferPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    public void expireAll() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(APPROVAL_TIMEOUT_MINUTES);
        List<Transfer> expiredTransfers = transferPort.findAwaitingApprovalOlderThan(cutoffTime);

        for (Transfer transfer : expiredTransfers) {
            transfer.setTransferStatus(TransferStatus.EXPIRED);
            transferPort.save(transfer);

            // Register expiration in audit log (NoSQL)
            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.TRANSFER_EXPIRED);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(transfer.getCreatorUserId());
            log.setUserRole("SYSTEM");
            log.setAffectedProductId(String.valueOf(transfer.getTransferId()));

            Map<String, Object> details = new HashMap<>();
            details.put("transferId", transfer.getTransferId());
            details.put("previousStatus", "AWAITING_APPROVAL");
            details.put("newStatus", "EXPIRED");
            details.put("createdAt", transfer.getCreationDateTime() != null ? transfer.getCreationDateTime().toString() : "N/A");
            details.put("expiredAt", LocalDateTime.now().toString());
            details.put("reason", "Vencida por falta de aprobación en el tiempo establecido (60 minutos)");
            details.put("amount", transfer.getAmount());
            details.put("creatorUserId", transfer.getCreatorUserId());
            log.setDetails(details);

            auditLogMongoPort.save(log);
        }
    }
}
