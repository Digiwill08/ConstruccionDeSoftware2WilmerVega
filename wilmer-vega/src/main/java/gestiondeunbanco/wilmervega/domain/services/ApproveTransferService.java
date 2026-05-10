package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.InsufficientBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Approves a high-value transfer that is AWAITING_APPROVAL.
 * Only a COMPANY_SUPERVISOR should invoke this service.
 * Validates: not expired, sufficient balance.
 * Transitions: AWAITING_APPROVAL -> EXECUTED
 */
public class ApproveTransferService {

    private static final long APPROVAL_TIMEOUT_MINUTES = 60;

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public ApproveTransferService(TransferPort transferPort, BankAccountPort bankAccountPort,
                                   AuditLogMongoPort auditLogMongoPort) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Transfer approve(Long transferId, Long supervisorUserId, String supervisorRole) {
        // 1. Find transfer
        Transfer transfer = transferPort.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + transferId));

        // 2. Validate status is AWAITING_APPROVAL
        if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
            throw new IllegalStateException("Transfer cannot be approved. Current status: "
                    + transfer.getTransferStatus() + ". Expected: AWAITING_APPROVAL");
        }

        // 3. Check if not expired (must be within 60 minutes)
        LocalDateTime expirationTime = transfer.getCreationDateTime().plusMinutes(APPROVAL_TIMEOUT_MINUTES);
        if (LocalDateTime.now().isAfter(expirationTime)) {
            // Auto-expire
            transfer.setTransferStatus(TransferStatus.EXPIRED);
            transferPort.save(transfer);
            throw new IllegalStateException("Transfer has expired. It was created at "
                    + transfer.getCreationDateTime() + " and the 60-minute window has passed.");
        }

        // 4. Find source account and validate balance
        BankAccount sourceAccount = bankAccountPort.findByAccountNumber(
                        transfer.getSourceAccount().getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Source account not found."));

        if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(
                    sourceAccount.getAccountNumber(), sourceAccount.getAccountStatus().name());
        }

        if (sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    sourceAccount.getCurrentBalance(), transfer.getAmount());
        }

        BigDecimal sourceBalanceBefore = sourceAccount.getCurrentBalance();

        // 5. Execute: deduct from source
        sourceAccount.setCurrentBalance(sourceBalanceBefore.subtract(transfer.getAmount()));
        bankAccountPort.save(sourceAccount);

        // 6. Add to destination (if internal)
        if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
            bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                    .ifPresent(dest -> {
                        dest.setCurrentBalance(dest.getCurrentBalance().add(transfer.getAmount()));
                        bankAccountPort.save(dest);
                    });
        }

        // 7. Update transfer
        transfer.setTransferStatus(TransferStatus.EXECUTED);
        transfer.setApprovalDateTime(LocalDateTime.now());
        transfer.setApproverUserId(supervisorUserId);
        Transfer savedTransfer = transferPort.save(transfer);

        // 8. Register in audit log
        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.TRANSFER_EXECUTED);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(supervisorUserId);
        log.setUserRole(supervisorRole);
        log.setAffectedProductId(String.valueOf(transferId));

        Map<String, Object> details = new HashMap<>();
        details.put("transferId", transferId);
        details.put("supervisorUserId", supervisorUserId);
        details.put("supervisorRole", supervisorRole);
        details.put("estadoAnterior", "AWAITING_APPROVAL");
        details.put("estadoNuevo", "EXECUTED");
        details.put("monto", transfer.getAmount());
        details.put("cuentaOrigen", sourceAccount.getAccountNumber());
        // ── Snapshot de Saldos (obligatorio) ──
        details.put("saldoOrigen_Antes", sourceBalanceBefore);
        details.put("saldoOrigen_Despues", sourceAccount.getCurrentBalance());
        details.put("fechaAprobacion", LocalDateTime.now().toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);

        return savedTransfer;
    }
}
