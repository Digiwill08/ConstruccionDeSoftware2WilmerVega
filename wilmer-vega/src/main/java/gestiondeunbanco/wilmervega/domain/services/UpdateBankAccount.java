package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

public class UpdateBankAccount {

    private final BankAccountPort bankAccountPort;

    public UpdateBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public BankAccount update(BankAccount bankAccount) {
        if (bankAccount == null || bankAccount.getId() == null) {
            throw new IllegalArgumentException("BankAccount or ID cannot be null for update");
        }
        if (bankAccountPort.findById(bankAccount.getId()).isEmpty()) {
            throw new NotFoundException("BankAccount not found for update");
        }
        return bankAccountPort.save(bankAccount);
    }
}
