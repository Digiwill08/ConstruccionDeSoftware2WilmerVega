package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository for BankAccount entity (Aggregate Root).
 * Defines persistence operations for bank accounts.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
    List<BankAccount> findByOwnerId(String ownerId);
    
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
}
