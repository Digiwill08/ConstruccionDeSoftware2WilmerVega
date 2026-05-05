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
        // 1. Validate transfer is not null
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer cannot be null.");
        }

        // 2. Validate amount > 0
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        // 3. Validate source account exists
        if (transfer.getSourceAccount() == null || transfer.getSourceAccount().getAccountNumber() == null) {
            throw new IllegalArgumentException("Source account must be specified.");
        }

        BankAccount sourceAccount = bankAccountPort.findByAccountNumber(transfer.getSourceAccount().getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Source account not found: " + transfer.getSourceAccount().getAccountNumber()));

        // 4. Validate source account is ACTIVE
        if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Source account is not ACTIVE. Status: " + sourceAccount.getAccountStatus());
        }

        // 5. Validate user has permission on source account
        if (sourceAccount.getHolder() == null || !sourceAccount.getHolder().getId().equals(transfer.getCreatorUserId())) {
            throw new UnauthorizedAccessException("User does not have permission to transfer from account: " + transfer.getSourceAccount().getAccountNumber());
        }

        // 6. Validate destination account exists and is ACTIVE (if specified)
        BankAccount destinationAccount = null;
        if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
            destinationAccount = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Destination account not found: " + transfer.getDestinationAccount().getAccountNumber()));
            
            if (destinationAccount.getAccountStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException("Destination account is not ACTIVE. Status: " + destinationAccount.getAccountStatus());
            }
        }

        // 7. Set creation time
        transfer.setCreationDateTime(LocalDateTime.now());
        // 8. Determine if transfer needs approval (enterprise threshold)
        if (transfer.getAmount().compareTo(ENTERPRISE_THRESHOLD) > 0) {
            // High amount: goes to awaiting approval
            transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
            Transfer saved = transferPort.save(transfer);
            registerAuditLog(saved, sourceAccount, null, "PENDING",
                    "AWAITING_APPROVAL", null, null, transfer.getCreatorUserId(), "COMPANY_EMPLOYEE");

            return saved;
        }

        // Low amount: execute immediately
        if (sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient balance in source account. Available: "
                    + sourceAccount.getCurrentBalance() + ", Required: " + transfer.getAmount());
        }

        BigDecimal originBalanceBefore = sourceAccount.getCurrentBalance();
        sourceAccount.setCurrentBalance(originBalanceBefore.subtract(transfer.getAmount()));
        bankAccountPort.save(sourceAccount);

        // If destination account is internal, update its balance
        if (destinationAccount != null) {
            destinationAccount.setCurrentBalance(destinationAccount.getCurrentBalance().add(transfer.getAmount()));
            bankAccountPort.save(destinationAccount);
        }

        transfer.setTransferStatus(TransferStatus.EXECUTED);
        Transfer saved = transferPort.save(transfer);

        registerAuditLog(saved, sourceAccount, destinationAccount, "PENDING", "EXECUTED",
                originBalanceBefore, sourceAccount.getCurrentBalance(),
                transfer.getCreatorUserId(), "CLIENT");

        return saved;
        
    }

    private void registerAuditLog(Transfer transfer, BankAccount sourceAccount,
                                   BankAccount destAccount, String prevStatus, String newStatus,
                                   BigDecimal balanceBefore, BigDecimal balanceAfter,
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
        details.put("previousStatus", prevStatus);
        details.put("newStatus", newStatus);
        if (balanceBefore != null) details.put("sourceBalanceBefore", balanceBefore);
        if (balanceAfter != null) details.put("sourceBalanceAfter", balanceAfter);
        log.setDetails(details);

        auditLogMongoPort.save(log);
    }
}

