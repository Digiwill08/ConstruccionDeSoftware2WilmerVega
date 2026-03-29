package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.services.BankAccountDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountUseCase {

    private final BankAccountDomainService bankAccountDomainService;

    public BankAccountUseCase(BankAccountPort bankAccountPort) {
        this.bankAccountDomainService = new BankAccountDomainService(bankAccountPort);
    }

    public List<BankAccount> findAll() {
        return bankAccountDomainService.findAll();
    }

    public Optional<BankAccount> findById(Long id) {
        return bankAccountDomainService.findById(id);
    }

    public BankAccount save(BankAccount bankAccount) {
        return bankAccountDomainService.save(bankAccount);
    }

    public void deleteById(Long id) {
        bankAccountDomainService.deleteById(id);
    }

    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return bankAccountDomainService.findByAccountNumber(accountNumber);
    }

    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return bankAccountDomainService.findByAccountStatus(status);
    }
}
