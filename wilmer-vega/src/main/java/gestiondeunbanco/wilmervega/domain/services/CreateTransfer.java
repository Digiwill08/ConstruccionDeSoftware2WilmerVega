package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.exceptions.UnauthorizedAccessException;
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
 * Creates and potentially executes a transfer with full business logic:
 * - Validates amount > 0
 * - Validates source account exists and is ACTIVE
 * - Validates sufficient balance for immediate execution
 * - Transfers below ENTERPRISE_THRESHOLD are executed immediately (EXECUTED)
 * - Transfers above the threshold go to AWAITING_APPROVAL
 */
public class CreateTransfer {

    private static final BigDecimal ENTERPRISE_THRESHOLD = new BigDecimal("10000000"); // 10,000,000 COP

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public CreateTransfer(TransferPort transferPort, BankAccountPort bankAccountPort,
                          AuditLogMongoPort auditLogMongoPort) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Transfer save(Transfer transfer) {
        try {
            if (transfer == null) {
                throw new IllegalArgumentException("Transfer cannot be null.");
            }

            if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be greater than zero.");
            }

            if (transfer.getSourceAccount() == null || transfer.getSourceAccount().getAccountNumber() == null) {
                throw new IllegalArgumentException("Source account must be specified.");
            }

            BankAccount sourceAccount = bankAccountPort.findByAccountNumber(transfer.getSourceAccount().getAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Source account not found: " + transfer.getSourceAccount().getAccountNumber()));

            if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException("Source account is not ACTIVE. Status: " + sourceAccount.getAccountStatus());
            }

            if (sourceAccount.getHolder() == null || !sourceAccount.getHolder().getId().equals(transfer.getCreatorUserId())) {
                throw new UnauthorizedAccessException("User does not have permission to transfer from account: " + transfer.getSourceAccount().getAccountNumber());
            }

            BankAccount destinationAccount = null;
            if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
                destinationAccount = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                        .orElseThrow(() -> new NotFoundException("Destination account not found: " + transfer.getDestinationAccount().getAccountNumber()));

                if (destinationAccount.getAccountStatus() != AccountStatus.ACTIVE) {
                    throw new IllegalStateException("Destination account is not ACTIVE. Status: " + destinationAccount.getAccountStatus());
                }
            }

            transfer.setCreationDateTime(LocalDateTime.now());
            if (transfer.getAmount().compareTo(ENTERPRISE_THRESHOLD) > 0) {
                transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
                Transfer saved = transferPort.save(transfer);
                registerAuditLog(saved, sourceAccount, destinationAccount, "PENDING",
                        "AWAITING_APPROVAL", null, null, null, null,
                        transfer.getCreatorUserId(), "COMPANY_EMPLOYEE");
                return saved;
            }

            if (sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
                throw new IllegalStateException("Insufficient balance in source account. Available: "
                        + sourceAccount.getCurrentBalance() + ", Required: " + transfer.getAmount());
            }

            BigDecimal originBalanceBefore = sourceAccount.getCurrentBalance();
            BigDecimal destinationBalanceBefore = destinationAccount != null ? destinationAccount.getCurrentBalance() : null;

            sourceAccount.setCurrentBalance(originBalanceBefore.subtract(transfer.getAmount()));
            bankAccountPort.save(sourceAccount);

            if (destinationAccount != null) {
                BigDecimal safeDestinationBalanceBefore = destinationBalanceBefore != null
                        ? destinationBalanceBefore
                        : BigDecimal.ZERO;
                destinationAccount.setCurrentBalance(safeDestinationBalanceBefore.add(transfer.getAmount()));
                bankAccountPort.save(destinationAccount);
            }

            transfer.setTransferStatus(TransferStatus.EXECUTED);
            Transfer saved = transferPort.save(transfer);

            registerAuditLog(saved, sourceAccount, destinationAccount, "PENDING", "EXECUTED",
                    originBalanceBefore, sourceAccount.getCurrentBalance(),
                    destinationBalanceBefore,
                    destinationAccount != null ? destinationAccount.getCurrentBalance() : null,
                    transfer.getCreatorUserId(), "CLIENT");

            return saved;
        } catch (RuntimeException ex) {
            registerFailure(transfer, ex);
            throw ex;
        }
        
    }

    private void registerAuditLog(Transfer transfer, BankAccount sourceAccount,
                                   BankAccount destAccount, String prevStatus, String newStatus,
                                   BigDecimal sourceBalanceBefore, BigDecimal sourceBalanceAfter,
                                   BigDecimal destinationBalanceBefore, BigDecimal destinationBalanceAfter,
                                   Long userId, String role) {
        AuditLog log = new AuditLog();
        // Use TRANSFER_INITIATED for pending approvals, TRANSFER_EXECUTED for immediate ones
        boolean isExecuted = "EXECUTED".equals(newStatus);
        log.setOperationType(isExecuted ? OperationType.TRANSFER_EXECUTED : OperationType.TRANSFER_INITIATED);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(userId);
        log.setUserRole(role);
        log.setAffectedProductId(String.valueOf(transfer.getTransferId()));

        Map<String, Object> details = new HashMap<>();
        details.put("transferId", transfer.getTransferId());
        details.put("amount", transfer.getAmount());
        details.put("sourceAccount", sourceAccount.getAccountNumber());
        details.put("destinationAccount", destAccount != null ? destAccount.getAccountNumber() : null);
        details.put("previousStatus", prevStatus);
        details.put("newStatus", newStatus);
        if (sourceBalanceBefore != null) details.put("sourceBalanceBefore", sourceBalanceBefore);
        if (sourceBalanceAfter != null) details.put("sourceBalanceAfter", sourceBalanceAfter);
        if (destinationBalanceBefore != null) details.put("destinationBalanceBefore", destinationBalanceBefore);
        if (destinationBalanceAfter != null) details.put("destinationBalanceAfter", destinationBalanceAfter);
        log.setDetails(details);

        auditLogMongoPort.save(log);
    }

    private void registerFailure(Transfer transfer, RuntimeException ex) {
        try {
            AuditLog log = new AuditLog();
            log.setOperationType(OperationType.SECURITY_VALIDATION_FAILURE);
            log.setOperationDateTime(LocalDateTime.now());
            log.setUserId(transfer != null ? transfer.getCreatorUserId() : null);
            log.setUserRole("CLIENT");
            log.setAffectedProductId(transfer != null && transfer.getTransferId() != null
                    ? String.valueOf(transfer.getTransferId())
                    : "N/A");

            Map<String, Object> details = new HashMap<>();
            details.put("operation", "TRANSFER_CREATE_OR_EXECUTE");
            details.put("transferId", transfer != null ? transfer.getTransferId() : null);
            details.put("sourceAccount", transfer != null && transfer.getSourceAccount() != null
                    ? transfer.getSourceAccount().getAccountNumber() : null);
            details.put("destinationAccount", transfer != null && transfer.getDestinationAccount() != null
                    ? transfer.getDestinationAccount().getAccountNumber() : null);
            details.put("amount", transfer != null ? transfer.getAmount() : null);
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

