package gestiondeunbanco.wilmervega.infrastructure.adapters.repository;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
