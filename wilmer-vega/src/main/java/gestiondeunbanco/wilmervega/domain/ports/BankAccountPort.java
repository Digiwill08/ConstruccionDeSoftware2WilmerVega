package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface BankAccountPort {
    List<BankAccount> findAll();
    Optional<BankAccount> findById(Long id);
    BankAccount save(BankAccount bankAccount);
    void deleteById(Long id);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByAccountStatus(AccountStatus status);
    boolean existsByAccountNumber(String accountNumber);
}
