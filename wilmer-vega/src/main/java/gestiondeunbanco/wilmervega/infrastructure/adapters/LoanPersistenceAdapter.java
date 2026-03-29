package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository loanRepository;

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void deleteById(Long id) {
        loanRepository.deleteById(id);
    }
}
