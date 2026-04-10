package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

import java.util.List;

public class FindBankAccount {

    private final BankAccountPort bankAccountPort;

    public FindBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public List<BankAccount> findAll() {
        return bankAccountPort.findAll();
    }

    public BankAccount findById(Long id) {
        return bankAccountPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Bank Account not found with ID: " + id));
    }

    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountPort.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank Account not found with account number: " + accountNumber));
    }

    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return bankAccountPort.findByAccountStatus(status);
    }
}
