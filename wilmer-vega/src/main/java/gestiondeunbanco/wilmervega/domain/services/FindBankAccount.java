package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

import java.util.List;
import java.util.Optional;

public class FindBankAccount {

    private final BankAccountPort bankAccountPort;

    public FindBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public List<BankAccount> findAll() {
        return bankAccountPort.findAll();
    }

    public Optional<BankAccount> findById(Long id) {
        return bankAccountPort.findById(id);
    }

    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return bankAccountPort.findByAccountNumber(accountNumber);
    }

    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return bankAccountPort.findByAccountStatus(status);
    }
}
