package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

import java.util.List;
import java.util.Optional;

public class FindLoan {

    private final LoanPort loanPort;

    public FindLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public List<Loan> findAll() {
        return loanPort.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return loanPort.findById(id);
    }
}
