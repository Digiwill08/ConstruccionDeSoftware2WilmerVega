package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;

public class DeleteBankAccount {

    private final BankAccountPort bankAccountPort;

    public DeleteBankAccount(BankAccountPort bankAccountPort) {
        this.bankAccountPort = bankAccountPort;
    }

    public void deleteById(Long id) {
        bankAccountPort.deleteById(id);
    }
}
