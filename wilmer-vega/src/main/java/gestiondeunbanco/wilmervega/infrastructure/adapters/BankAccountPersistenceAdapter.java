package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void deleteById(Long id) {
        bankAccountRepository.deleteById(id);
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return bankAccountRepository.findByAccountStatus(status);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return bankAccountRepository.existsByAccountNumber(accountNumber);
    }
}
