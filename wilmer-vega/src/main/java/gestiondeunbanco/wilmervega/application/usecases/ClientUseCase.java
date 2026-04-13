package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.services.CreateTransfer;
import gestiondeunbanco.wilmervega.domain.services.FindBankAccount;
import gestiondeunbanco.wilmervega.domain.services.FindTransfer;

import java.util.List;

public class ClientUseCase {

    private final FindBankAccount findBankAccount;
    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;

    public ClientUseCase(FindBankAccount findBankAccount, CreateTransfer createTransfer, FindTransfer findTransfer) {
        this.findBankAccount = findBankAccount;
        this.createTransfer = createTransfer;
        this.findTransfer = findTransfer;
    }

    public BankAccount findMyBankAccount(String accountNumber) {
        return findBankAccount.findByAccountNumber(accountNumber);
    }
    
    public Transfer executeTransfer(Transfer transfer) {
        return createTransfer.save(transfer);
    }
    
    public Transfer findTransferById(Long id) {
        return findTransfer.findById(id);
    }
    
    public List<Transfer> findAllTransfers() {
        return findTransfer.findAll();
    }
}
