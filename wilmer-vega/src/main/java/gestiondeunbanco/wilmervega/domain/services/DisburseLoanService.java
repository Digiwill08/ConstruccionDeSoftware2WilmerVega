package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.exceptions.OwnershipViolationException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Desembolsa un prestamo APROBADO a la cuenta bancaria del cliente.
 * Transicion de estado: APPROVED -> DISBURSED
 *
 * Reglas de negocio:
 *   - El prestamo debe estar en estado APPROVED
 *   - El monto aprobado debe ser mayor a cero
 *   - La cuenta destino debe pertenecer al cliente solicitante (clientApplicant)
 *   - La cuenta destino debe estar ACTIVA
 *   - Operacion atomica con rollback automatico (@Transactional)
 *   - Snapshot de saldos antes/despues registrado en bitacora NoSQL
 */
public class DisburseLoanService {

    private final LoanPort loanPort;
    private final BankAccountPort bankAccountPort;
    private final AuditLogMongoPort auditLogMongoPort;

    public DisburseLoanService(LoanPort loanPort, BankAccountPort bankAccountPort,
                               AuditLogMongoPort auditLogMongoPort) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.auditLogMongoPort = auditLogMongoPort;
    }

    @Transactional
    public Loan disburse(Long loanId, Long disbursementAccountId, Long analystUserId, String analystRole) {

        // 1. Buscar prestamo — falla rapido si no existe
        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Prestamo no encontrado con ID: " + loanId));

        // 2. Estado debe ser APPROVED
        if (loan.getLoanStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException(
                    "El prestamo no puede ser desembolsado. Estado actual: " + loan.getLoanStatus()
                    + ". Estado requerido: APPROVED");
        }

        // 3. Monto aprobado > 0
        if (loan.getApprovedAmount() == null || loan.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El monto aprobado debe ser mayor a cero para poder realizar el desembolso.");
        }

        // 4. Buscar cuenta destino
        BankAccount account = bankAccountPort.findById(disbursementAccountId)
                .orElseThrow(() -> new NotFoundException(
                        "Cuenta de desembolso no encontrada con ID: " + disbursementAccountId));

        // 5. La cuenta debe pertenecer al cliente solicitante del prestamo
        if (loan.getClientApplicant() != null && loan.getClientApplicant().getId() != null
                && account.getHolder() != null && account.getHolder().getId() != null) {
            if (!loan.getClientApplicant().getId().equals(account.getHolder().getId())) {
                throw new OwnershipViolationException(
                        "La cuenta de desembolso (ID: " + disbursementAccountId
                        + ") no pertenece al cliente solicitante del prestamo (Cliente ID: "
                        + loan.getClientApplicant().getId() + "). Operacion rechazada.");
            }
        }

        // 6. Cuenta debe estar ACTIVA
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(
                    account.getAccountNumber(), account.getAccountStatus().name());
        }

        BigDecimal balanceBefore = account.getCurrentBalance();

        // 7. Acreditar saldo (operacion atomica)
        account.setCurrentBalance(balanceBefore.add(loan.getApprovedAmount()));
        bankAccountPort.save(account);

        // 8. Actualizar estado del prestamo
        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDate.now());
        loan.setDisbursementAccount(account);
        Loan savedLoan = loanPort.save(loan);

        // 9. Bitacora NoSQL — snapshot de saldos obligatorio
        AuditLog log = new AuditLog();
        log.setOperationType(OperationType.LOAN_DISBURSEMENT);
        log.setOperationDateTime(LocalDateTime.now());
        log.setUserId(analystUserId);
        log.setUserRole(analystRole);
        log.setAffectedProductId(String.valueOf(loanId));

        Map<String, Object> details = new HashMap<>();
        details.put("loanId", loanId);
        details.put("analystUserId", analystUserId);
        details.put("analystRole", analystRole);
        details.put("estadoAnterior", "APPROVED");
        details.put("estadoNuevo", "DISBURSED");
        details.put("montoDesembolsado", loan.getApprovedAmount());
        details.put("cuentaDestino", account.getAccountNumber());
        details.put("titularCuentaId", account.getHolder() != null ? account.getHolder().getId() : null);
        details.put("saldoAntes", balanceBefore);
        details.put("saldoDespues", account.getCurrentBalance());
        details.put("fechaDesembolso", LocalDate.now().toString());
        details.put("fechaHoraOperacion", LocalDateTime.now().toString());
        log.setDetails(details);

        auditLogMongoPort.save(log);
        return savedLoan;
    }
}
