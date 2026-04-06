package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

public class DeleteLoan {

    private final LoanPort loanPort;

    public DeleteLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public void deleteById(Long id) {
        loanPort.deleteById(id);
    }
}
