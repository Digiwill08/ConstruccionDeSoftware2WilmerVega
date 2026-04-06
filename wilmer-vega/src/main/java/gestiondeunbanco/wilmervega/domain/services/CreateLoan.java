package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

public class CreateLoan {

    private final LoanPort loanPort;

    public CreateLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public Loan save(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        return loanPort.save(loan);
    }
}
