package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LoanDomainService {

    private final LoanPort loanPort;

    public LoanDomainService(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public List<Loan> findAll() {
        return loanPort.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return loanPort.findById(id);
    }

    public Loan save(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        if (loan.getRequestedAmount() != null && loan.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Requested loan amount must be positive");
        }
        return loanPort.save(loan);
    }

    public void deleteById(Long id) {
        loanPort.deleteById(id);
    }
}
