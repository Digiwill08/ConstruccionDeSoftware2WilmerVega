package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import gestiondeunbanco.wilmervega.domain.services.LoanDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanUseCase {

    private final LoanDomainService loanDomainService;

    public LoanUseCase(LoanPort loanPort) {
        this.loanDomainService = new LoanDomainService(loanPort);
    }

    public List<Loan> findAll() {
        return loanDomainService.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return loanDomainService.findById(id);
    }

    public Loan save(Loan loan) {
        return loanDomainService.save(loan);
    }

    public void deleteById(Long id) {
        loanDomainService.deleteById(id);
    }
}
