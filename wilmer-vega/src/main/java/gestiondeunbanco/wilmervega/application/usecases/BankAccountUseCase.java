package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.services.CreateBankAccount;
import gestiondeunbanco.wilmervega.domain.services.FindBankAccount;
import gestiondeunbanco.wilmervega.domain.services.UpdateBankAccount;

import java.util.List;

public class BankAccountUseCase {

    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;
    private final UpdateBankAccount updateBankAccount;

    public BankAccountUseCase(CreateBankAccount createBankAccount,
                              FindBankAccount findBankAccount,
                              UpdateBankAccount updateBankAccount) {
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
        this.updateBankAccount = updateBankAccount;
    }

    public BankAccount create(BankAccount account) {
        return createBankAccount.save(account);
    }

    public List<BankAccount> findAll() {
        return findBankAccount.findAll();
    }

    public List<BankAccount> findByHolderId(Long holderId) {
        return findBankAccount.findByHolderId(holderId);
    }

    public BankAccount findByAccountNumber(String accountNumber) {
        return findBankAccount.findByAccountNumber(accountNumber);
    }

    public BankAccount findById(Long id) {
        return findBankAccount.findById(id);
    }

    public BankAccount updateStatus(Long id, AccountStatus status) {
        BankAccount current = findById(id);
        current.setAccountStatus(status);
        return updateBankAccount.update(current);
    }
}
