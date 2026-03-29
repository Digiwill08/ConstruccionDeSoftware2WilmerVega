package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

import java.util.List;
import java.util.Optional;

public class BankAccountDomainService {

    private final BankAccountPort bankAccountPort;

    public BankAccountDomainService(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public List<BankAccount> findAll() {
        return bankAccountPort.findAll();
    }

    public Optional<BankAccount> findById(Long id) {
        return bankAccountPort.findById(id);
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

    public void deleteById(Long id) {
        bankAccountPort.deleteById(id);
    }

    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return bankAccountPort.findByAccountNumber(accountNumber);
    }

    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return bankAccountPort.findByAccountStatus(status);
    }
}
