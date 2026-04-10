package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

public class DeleteLoan {

    private final LoanPort loanPort;

    public DeleteLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public void deleteById(Long id) {
        if (loanPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: Loan not found with ID " + id);
        }
        loanPort.deleteById(id);
    }
}
