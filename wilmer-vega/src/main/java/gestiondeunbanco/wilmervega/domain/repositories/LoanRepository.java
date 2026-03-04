package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.Loan;
import gestiondeunbanco.wilmervega.domain.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Domain repository for Loan entity (Aggregate Root).
 * Defines persistence operations for loans.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    List<Loan> findByApplicantClientId(String applicantClientId);
    
    List<Loan> findByLoanStatus(LoanStatus loanStatus);
    
    List<Loan> findByApplicantClientIdAndLoanStatus(
        String applicantClientId, LoanStatus loanStatus);
}
