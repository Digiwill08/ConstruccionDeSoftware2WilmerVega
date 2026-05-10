package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Domain service for creating bank accounts.
 * Security rules (Vinculo de Propiedad):
 *  - ID_Titular obligatorio y debe referenciar a un cliente EXISTENTE y ACTIVO
 *  - Prohibida la creacion de cuentas sin dueno rastreable
 */
public class CreateBankAccount {

    private final BankAccountPort bankAccountPort;
    private final NaturalClientPort naturalClientPort;
    private final CompanyClientPort companyClientPort;

    public CreateBankAccount(BankAccountPort bankAccountPort,
                             NaturalClientPort naturalClientPort,
                             CompanyClientPort companyClientPort) {
        this.bankAccountPort = bankAccountPort;
        this.naturalClientPort = naturalClientPort;
        this.companyClientPort = companyClientPort;
    }

    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new IllegalArgumentException("La cuenta bancaria no puede ser nula");
        }
        if (bankAccount.getAccountNumber() == null || bankAccount.getAccountNumber().isBlank()) {
            throw new IllegalArgumentException("El numero de cuenta es obligatorio");
        }
        if (bankAccount.getAccountType() == null) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        }
        if (bankAccount.getCurrency() == null) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }
        if (bankAccount.getAccountStatus() == null) {
            throw new IllegalArgumentException("El estado de la cuenta es obligatorio");
        }
        if (bankAccount.getOpeningDate() == null) {
            throw new IllegalArgumentException("La fecha de apertura es obligatoria");
        }
        if (bankAccount.getCurrentBalance() == null
                || bankAccount.getCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor o igual a cero");
        }

        // ── Vinculo de Propiedad: titular obligatorio y rastreable ──────────────
        if (bankAccount.getHolder() == null || bankAccount.getHolder().getId() == null) {
            throw new IllegalArgumentException(
                    "Toda cuenta bancaria debe tener un titular. Creacion sin dueno rastreable es prohibida.");
        }

        Long holderId = bankAccount.getHolder().getId();

        // Verifica existencia del titular (Natural o Empresa)
        Optional<NaturalClient> naturalOpt = naturalClientPort.findById(holderId);
        Optional<CompanyClient> companyOpt = companyClientPort.findById(holderId);

        if (naturalOpt.isEmpty() && companyOpt.isEmpty()) {
            throw new NotFoundException(
                    "No se encontro ningun cliente activo con ID: " + holderId
                    + ". No se puede crear una cuenta sin titular existente.");
        }

        // ── Unicidad del numero de cuenta ──────────────────────────────────
        if (bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "Ya existe una cuenta con el numero: " + bankAccount.getAccountNumber());
        }

        return bankAccountPort.save(bankAccount);
    }
}
