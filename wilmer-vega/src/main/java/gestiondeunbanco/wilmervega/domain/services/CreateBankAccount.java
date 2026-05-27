package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateAccountNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidBankAccountException;
import gestiondeunbanco.wilmervega.domain.exceptions.MissingAccountHolderException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Servicio de dominio para crear cuentas bancarias.
 * Reglas de seguridad (Vinculo de Propiedad):
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

    @Transactional
    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new InvalidBankAccountException("La cuenta bancaria no puede ser nula");
        }
        if (bankAccount.getAccountNumber() == null || bankAccount.getAccountNumber().isBlank()) {
            throw new InvalidBankAccountException("El numero de cuenta es obligatorio");
        }
        if (bankAccount.getAccountType() == null) {
            throw new InvalidBankAccountException("El tipo de cuenta es obligatorio");
        }
        if (bankAccount.getCurrency() == null) {
            throw new InvalidBankAccountException("La moneda es obligatoria");
        }
        if (bankAccount.getAccountStatus() == null) {
            throw new InvalidBankAccountException("El estado de la cuenta es obligatorio");
        }
        if (bankAccount.getOpeningDate() == null) {
            throw new InvalidBankAccountException("La fecha de apertura es obligatoria");
        }
        if (bankAccount.getCurrentBalance() == null
                || bankAccount.getCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBankAccountException("El saldo inicial debe ser mayor o igual a cero");
        }

        // Vinculo de Propiedad: titular obligatorio y rastreable
        if (bankAccount.getHolder() == null || bankAccount.getHolder().getId() == null) {
            throw new MissingAccountHolderException(
                    "Toda cuenta bancaria debe tener un titular. Creacion sin dueno rastreable es prohibida.");
        }

        Long holderId = bankAccount.getHolder().getId();

        Optional<NaturalClient> naturalOpt = naturalClientPort.findById(holderId);
        Optional<CompanyClient> companyOpt = companyClientPort.findById(holderId);

        if (naturalOpt.isEmpty() && companyOpt.isEmpty()) {
            throw new MissingAccountHolderException(
                    "No se encontro ningun cliente activo con ID: " + holderId
                    + ". No se puede crear una cuenta sin titular existente.");
        }

        // Unicidad del numero de cuenta
        if (bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new DuplicateAccountNumberException(
                    "Ya existe una cuenta con el numero: " + bankAccount.getAccountNumber());
        }

        return bankAccountPort.save(bankAccount);
    }
}
