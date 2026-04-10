package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

public class UpdateLoan {

    private final LoanPort loanPort;

    public UpdateLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public Loan update(Loan loan) {
        if (loan == null || loan.getLoanId() == null) {
            throw new IllegalArgumentException("Loan or Loan ID cannot be null for update");
        }
        if (loanPort.findById(loan.getLoanId()).isEmpty()) {
            throw new NotFoundException("Loan not found for update");
        }
        return loanPort.save(loan);
    }
}
