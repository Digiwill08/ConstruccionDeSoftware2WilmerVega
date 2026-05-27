package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.InsufficientBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.exceptions.TransferNotApprovableException;
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
 * Aprueba una transferencia empresarial en estado AWAITING_APPROVAL.
 * Solo el rol COMPANY_SUPERVISOR puede invocar este servicio.
 * Transicion: AWAITING_APPROVAL -> EXECUTED
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

        LocalDateTime operationDateTime = LocalDateTime.now();

        Transfer transfer = transferPort.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transferencia no encontrada con ID: " + transferId));

        if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
            throw new TransferNotApprovableException("La transferencia no puede ser aprobada. Estado actual: "
                + transfer.getTransferStatus() + ". Estado requerido: AWAITING_APPROVAL");
        }

        // Ventana de aprobacion de 60 minutos
        LocalDateTime expirationTime = transfer.getCreationDateTime().plusMinutes(APPROVAL_TIMEOUT_MINUTES);
        if (operationDateTime.isAfter(expirationTime)) {
            transfer.setTransferStatus(TransferStatus.EXPIRED);
            transferPort.save(transfer);
            throw new TransferNotApprovableException("La transferencia ha expirado. Fue creada en "
                + transfer.getCreationDateTime() + " y ya paso la ventana de 60 minutos.");
        }

        BankAccount sourceAccount = bankAccountPort.findByAccountNumber(
                        transfer.getSourceAccount().getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Cuenta origen no encontrada."));

        // Cuenta debe estar ACTIVA
        if (sourceAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(
                    sourceAccount.getAccountNumber(), sourceAccount.getAccountStatus().name());
        }

        // Saldo suficiente (validar null-safe)
        if (sourceAccount.getCurrentBalance() == null
            || sourceAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                sourceAccount.getCurrentBalance(), transfer.getAmount());
        }

        BigDecimal sourceBalanceBefore = sourceAccount.getCurrentBalance();
        sourceAccount.setCurrentBalance(sourceBalanceBefore.subtract(transfer.getAmount()));
        bankAccountPort.save(sourceAccount);

        BigDecimal destinationBalanceBefore = null;
        BigDecimal destinationBalanceAfter = null;
        String destinationAccountNumber = null;

        if (transfer.getDestinationAccount() != null && transfer.getDestinationAccount().getAccountNumber() != null) {
            BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount().getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Cuenta destino no encontrada."));
            destinationBalanceBefore = destination.getCurrentBalance() != null ? destination.getCurrentBalance() : BigDecimal.ZERO;
            destination.setCurrentBalance(destinationBalanceBefore.add(transfer.getAmount()));
            bankAccountPort.save(destination);
            destinationBalanceAfter = destination.getCurrentBalance();
            destinationAccountNumber = destination.getAccountNumber();
        }

        transfer.setTransferStatus(TransferStatus.EXECUTED);
        transfer.setApprovalDateTime(operationDateTime);
        transfer.setApproverUserId(supervisorUserId);
        Transfer savedTransfer = transferPort.save(transfer);

        // Bitacora NoSQL — snapshot de saldos obligatorio
        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.TRANSFER_EXECUTED);
        log.setOperationDateTime(operationDateTime);
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
        details.put("cuentaDestino", destinationAccountNumber);
        details.put("saldoOrigen_Antes", sourceBalanceBefore);
        details.put("saldoOrigen_Despues", sourceAccount.getCurrentBalance());
        details.put("saldoDestino_Antes", destinationBalanceBefore);
        details.put("saldoDestino_Despues", destinationBalanceAfter);
        details.put("fechaAprobacion", operationDateTime.toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);
        return savedTransfer;
    }
}
