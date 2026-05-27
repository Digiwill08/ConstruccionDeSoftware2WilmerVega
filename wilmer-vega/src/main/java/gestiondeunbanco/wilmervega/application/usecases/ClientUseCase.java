package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.config.security.ClientAccessContext;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
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
        BankAccount account = findBankAccount.findByAccountNumber(accountNumber);
        enforceClientAccountOwnership(account);
        return account;
    }
    
    public Transfer executeTransfer(Transfer transfer) {
        transfer.setCreatorUserId(ClientAccessContext.getCurrentUserId());
        return createTransfer.save(transfer);
    }
    
    public Transfer findTransferById(Long id) {
        Transfer transfer = findTransfer.findById(id);
        enforceClientTransferOwnership(transfer);
        return transfer;
    }
    
    public List<Transfer> findAllTransfers() {
        if (!isClientRole(ClientAccessContext.getCurrentRole())) {
            return findTransfer.findAll();
        }

        Long currentClientId = ClientAccessContext.getCurrentClientId();
        if (currentClientId == null) {
            throw new IllegalStateException("Authenticated client does not have related client ID");
        }
        return findTransfer.findBySourceHolderId(currentClientId);
    }

    private void enforceClientTransferOwnership(Transfer transfer) {
        if (!isClientRole(ClientAccessContext.getCurrentRole())) {
            return;
        }

        Long currentClientId = ClientAccessContext.getCurrentClientId();
        Long ownerClientId = transfer != null
                && transfer.getSourceAccount() != null
                && transfer.getSourceAccount().getHolder() != null
                ? transfer.getSourceAccount().getHolder().getId()
                : null;

        if (currentClientId == null || ownerClientId == null || !currentClientId.equals(ownerClientId)) {
            throw new IllegalStateException("No autorizado para acceder a transferencias de otros titulares");
        }
    }

    private void enforceClientAccountOwnership(BankAccount account) {
        if (!isClientRole(ClientAccessContext.getCurrentRole())) {
            return;
        }

        Long currentClientId = ClientAccessContext.getCurrentClientId();
        Long ownerClientId = account != null && account.getHolder() != null ? account.getHolder().getId() : null;
        if (currentClientId == null || ownerClientId == null || !currentClientId.equals(ownerClientId)) {
            throw new IllegalStateException("No autorizado para acceder a cuentas de otros titulares");
        }
    }

    private boolean isClientRole(String role) {
        return role != null && (
                role.equals(SystemRole.NATURAL_CLIENT.name())
                        || role.equals(SystemRole.COMPANY_CLIENT.name())
                        || role.equals(SystemRole.COMPANY_EMPLOYEE.name())
        );
    }
}
