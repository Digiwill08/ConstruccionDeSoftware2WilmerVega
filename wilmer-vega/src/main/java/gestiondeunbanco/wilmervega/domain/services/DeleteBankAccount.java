package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

public class DeleteBankAccount {

    private final BankAccountPort bankAccountPort;

    public DeleteBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public void deleteById(Long id) {
        if (bankAccountPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: BankAccount not found with ID " + id);
        }
        bankAccountPort.deleteById(id);
    }
}
