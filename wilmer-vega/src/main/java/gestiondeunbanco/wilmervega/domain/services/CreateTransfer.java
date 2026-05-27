package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidTransferBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidTransferRequestException;
import gestiondeunbanco.wilmervega.domain.exceptions.InsufficientBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.OperationType;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Crea y ejecuta (o encola) transferencias con logica de negocio completa:
 * - Monto > 0
 * - Cuenta origen ACTIVA
 * - Saldo suficiente para ejecucion inmediata
 * - Transferencias < umbral: ejecucion inmediata (EXECUTED)
 * - Transferencias >= umbral: pendiente de aprobacion (AWAITING_APPROVAL)
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
        LocalDateTime operationDateTime = LocalDateTime.now();

        if (transfer == null) {
            throw new InvalidTransferRequestException("La transferencia no puede ser nula.");
        }
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferRequestException("El monto de la transferencia debe ser mayor a cero.");
        }
        if (transfer.getSourceAccount() == null || transfer.getSourceAccount().getAccountNumber() == null) {
            throw new InvalidTransferRequestException("Se debe especificar la cuenta origen.");
        }

        BankAccount sourceAccount = bankAccountPort.findByAccountNumber(transfer.getSourceAccount().getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Cuenta origen no encontrada: " + transfer.getSourceAccount().getAccountNumber()));

        // Cuenta origen debe estar ACTIVA
        if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(
                    sourceAccount.getAccountNumber(), sourceAccount.getAccountStatus().name());
        }

        // Cuenta destino (si aplica)
        BankAccount destinationAccount = null;
        if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
            destinationAccount = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Cuenta destino no encontrada: " + transfer.getDestinationAccount().getAccountNumber()));

            if (destinationAccount.getAccountStatus() != AccountStatus.ACTIVE) {
                throw new AccountBlockedException(
                        destinationAccount.getAccountNumber(), destinationAccount.getAccountStatus().name());
            }
        }

        transfer.setCreationDateTime(operationDateTime);

        // Montos grandes: pendiente de aprobacion del supervisor
        if (transfer.getAmount().compareTo(ENTERPRISE_THRESHOLD) >= 0) {
            transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
            Transfer saved = transferPort.save(transfer);
            registerAuditLog(saved, sourceAccount, destinationAccount,
                    "PENDING", "AWAITING_APPROVAL",
                    null, null, null, null,
                    transfer.getCreatorUserId(), "COMPANY_EMPLOYEE", operationDateTime);
            return saved;
        }

        // Monto bajo: ejecutar inmediatamente — validar saldo suficiente
        if (sourceAccount.getCurrentBalance() == null) {
            throw new InvalidTransferBalanceException("La cuenta origen no tiene un saldo valido para ejecutar la transferencia.");
        }
        if (sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    sourceAccount.getCurrentBalance(), transfer.getAmount());
        }

        BigDecimal originBalanceBefore = sourceAccount.getCurrentBalance();
        BigDecimal destinationBalanceBefore = destinationAccount != null ? destinationAccount.getCurrentBalance() : null;

        sourceAccount.setCurrentBalance(originBalanceBefore.subtract(transfer.getAmount()));
        bankAccountPort.save(sourceAccount);

        if (destinationAccount != null) {
            BigDecimal safeDestBefore = destinationBalanceBefore != null ? destinationBalanceBefore : BigDecimal.ZERO;
            destinationAccount.setCurrentBalance(safeDestBefore.add(transfer.getAmount()));
            bankAccountPort.save(destinationAccount);
        }

        transfer.setTransferStatus(TransferStatus.EXECUTED);
        Transfer saved = transferPort.save(transfer);

        registerAuditLog(saved, sourceAccount, destinationAccount,
                "PENDING", "EXECUTED",
                originBalanceBefore, sourceAccount.getCurrentBalance(),
                destinationBalanceBefore,
                destinationAccount != null ? destinationAccount.getCurrentBalance() : null,
            transfer.getCreatorUserId(), "CLIENT", operationDateTime);

        return saved;
    }

    private void registerAuditLog(Transfer transfer, BankAccount sourceAccount,
                                   BankAccount destAccount, String prevStatus, String newStatus,
                                   BigDecimal sourceBalanceBefore, BigDecimal sourceBalanceAfter,
                                   BigDecimal destinationBalanceBefore, BigDecimal destinationBalanceAfter,
                                   Long userId, String role, LocalDateTime operationDateTime) {
        AuditLog log = new AuditLog();
        boolean isExecuted = "EXECUTED".equals(newStatus);
        log.setOperationType(isExecuted ? OperationType.TRANSFER_EXECUTED : OperationType.TRANSFER_INITIATED);
        log.setOperationDateTime(operationDateTime);
        log.setUserId(userId);
        log.setUserRole(role);
        log.setAffectedProductId(String.valueOf(transfer.getTransferId()));

        Map<String, Object> details = new HashMap<>();
        details.put("transferId", transfer.getTransferId());
        details.put("monto", transfer.getAmount());
        details.put("cuentaOrigen", sourceAccount.getAccountNumber());
        details.put("cuentaDestino", destAccount != null ? destAccount.getAccountNumber() : null);
        details.put("estadoAnterior", prevStatus);
        details.put("estadoNuevo", newStatus);
        if (sourceBalanceBefore != null)      details.put("saldoOrigen_Antes", sourceBalanceBefore);
        if (sourceBalanceAfter != null)       details.put("saldoOrigen_Despues", sourceBalanceAfter);
        if (destinationBalanceBefore != null) details.put("saldoDestino_Antes", destinationBalanceBefore);
        if (destinationBalanceAfter != null)  details.put("saldoDestino_Despues", destinationBalanceAfter);
        details.put("fechaHoraOperacion", operationDateTime.toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);
    }
}
