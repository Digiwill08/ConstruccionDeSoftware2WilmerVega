package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

import java.math.BigDecimal;

public class CreateBankAccount {

    private final BankAccountPort bankAccountPort;
    private final NaturalClientPort naturalClientPort;
    private final CompanyClientPort companyClientPort;
    private final UserPort userPort;

    public CreateBankAccount(BankAccountPort bankAccountPort,
                             NaturalClientPort naturalClientPort,
                             CompanyClientPort companyClientPort,
                             UserPort userPort) {
        this.bankAccountPort = bankAccountPort;
        this.naturalClientPort = naturalClientPort;
        this.companyClientPort = companyClientPort;
        this.userPort = userPort;
    }

    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new IllegalArgumentException("BankAccount cannot be null");
        }
        if (bankAccount.getAccountNumber() == null || bankAccount.getAccountNumber().isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (bankAccount.getAccountType() == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        if (bankAccount.getCurrency() == null) {
            throw new IllegalArgumentException("Currency is required");
        }
        if (bankAccount.getAccountStatus() == null) {
            throw new IllegalArgumentException("Account status is required");
        }
        if (bankAccount.getOpeningDate() == null) {
            throw new IllegalArgumentException("Opening date is required");
        }
        if (bankAccount.getCurrentBalance() == null || bankAccount.getCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance must be greater or equal to zero");
        }
        if (bankAccount.getHolder() == null || bankAccount.getHolder().getId() == null) {
            throw new IllegalArgumentException("Account holder is required");
        }
        Long holderId = bankAccount.getHolder().getId();
        boolean holderExists = naturalClientPort.findById(holderId).isPresent() || companyClientPort.findById(holderId).isPresent();
        if (!holderExists) {
            throw new IllegalArgumentException("Holder does not exist");
        }
        if (!userPort.existsActiveByRelatedClientId(holderId)) {
            throw new IllegalArgumentException("Holder must have at least one ACTIVE user linked before account creation");
        }
        if (bankAccount.getAccountNumber() != null && bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new IllegalArgumentException("A BankAccount with this account number already exists");
        }
        return bankAccountPort.save(bankAccount);
    }
}
