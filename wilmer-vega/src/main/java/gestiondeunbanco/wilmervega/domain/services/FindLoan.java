package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

import java.util.List;

public class FindLoan {

    private final LoanPort loanPort;

    public FindLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public List<Loan> findAll() {
        return loanPort.findAll();
    }

    public Loan findById(Long id) {
        return loanPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + id));
    }
}
