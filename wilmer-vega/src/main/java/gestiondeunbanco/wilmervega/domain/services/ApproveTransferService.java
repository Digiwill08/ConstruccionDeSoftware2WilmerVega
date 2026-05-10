package gestiondeunbanco.wilmervega.domain.services;

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
        try {
            validateSupervisorRole(supervisorRole);

            Transfer transfer = transferPort.findById(transferId)
                    .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + transferId));

            if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
                throw new IllegalStateException("Transfer cannot be approved. Current status: "
                        + transfer.getTransferStatus() + ". Expected: AWAITING_APPROVAL");
            }

            LocalDateTime expirationTime = transfer.getCreationDateTime().plusMinutes(APPROVAL_TIMEOUT_MINUTES);
            if (LocalDateTime.now().isAfter(expirationTime)) {
                transfer.setTransferStatus(TransferStatus.EXPIRED);
                transferPort.save(transfer);
                throw new IllegalStateException("Transfer has expired. It was created at "
                        + transfer.getCreationDateTime() + " and the 60-minute window has passed.");
            }

            BankAccount sourceAccount = bankAccountPort.findByAccountNumber(
                            transfer.getSourceAccount().getAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Source account not found."));

            if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException("Source account is no longer ACTIVE.");
            }

            if (sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
                throw new IllegalStateException("Insufficient balance. Available: "
                        + sourceAccount.getCurrentBalance() + ", Required: " + transfer.getAmount());
            }

            BigDecimal sourceBalanceBefore = sourceAccount.getCurrentBalance();
            sourceAccount.setCurrentBalance(sourceBalanceBefore.subtract(transfer.getAmount()));
            bankAccountPort.save(sourceAccount);

            BigDecimal destinationBalanceBefore = null;
            BigDecimal destinationBalanceAfter = null;
            String destinationAccount = null;
            if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
                BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                        .orElseThrow(() -> new NotFoundException("Destination account not found."));
                destinationBalanceBefore = destination.getCurrentBalance();
                destination.setCurrentBalance(destinationBalanceBefore.add(transfer.getAmount()));
                bankAccountPort.save(destination);
                destinationBalanceAfter = destination.getCurrentBalance();
                destinationAccount = destination.getAccountNumber();
            }

            transfer.setTransferStatus(TransferStatus.EXECUTED);
            transfer.setApprovalDateTime(LocalDateTime.now());
            transfer.setApproverUserId(supervisorUserId);
            Transfer savedTransfer = transferPort.save(transfer);

            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.TRANSFER_EXECUTED);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(supervisorUserId);
            log.setUserRole(supervisorRole);
            log.setAffectedProductId(String.valueOf(transferId));

            Map<String, Object> details = new HashMap<>();
            details.put("transferId", transferId);
            details.put("supervisorUserId", supervisorUserId);
            details.put("previousStatus", "AWAITING_APPROVAL");
            details.put("newStatus", "EXECUTED");
            details.put("amount", transfer.getAmount());
            details.put("sourceAccount", sourceAccount.getAccountNumber());
            details.put("destinationAccount", destinationAccount);
            details.put("sourceBalanceBefore", sourceBalanceBefore);
            details.put("sourceBalanceAfter", sourceAccount.getCurrentBalance());
            details.put("destinationBalanceBefore", destinationBalanceBefore);
            details.put("destinationBalanceAfter", destinationBalanceAfter);
            details.put("approvalDateTime", LocalDateTime.now().toString());
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
            throw new IllegalStateException("Only COMPANY_SUPERVISOR can approve high-value transfers");
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
            details.put("operation", "TRANSFER_APPROVAL");
            details.put("transferId", transferId);
            details.put("success", false);
            details.put("errorType", ex.getClass().getSimpleName());
            details.put("errorMessage", ex.getMessage());
            log.setDetails(details);

            auditLogMongoPort.save(log);
        } catch (Exception ignored) {
            // Do not block rollback flow if audit persistence is temporarily unavailable.
        }
    }
}
