package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanPort {
    List<Loan> findAll();
    Optional<Loan> findById(Long id);
    Loan save(Loan loan);
    void deleteById(Long id);
}
