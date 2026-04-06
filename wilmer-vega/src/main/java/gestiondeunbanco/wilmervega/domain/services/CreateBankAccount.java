package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

public class CreateBankAccount {

    private final BankAccountPort bankAccountPort;

    public CreateBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new IllegalArgumentException("BankAccount cannot be null");
        }
        if (bankAccount.getAccountNumber() != null && bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new IllegalArgumentException("A BankAccount with this account number already exists");
        }
        return bankAccountPort.save(bankAccount);
    }
}
