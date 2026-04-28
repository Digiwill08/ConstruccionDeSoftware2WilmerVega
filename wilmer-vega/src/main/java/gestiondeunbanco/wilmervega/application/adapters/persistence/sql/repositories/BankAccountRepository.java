package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    Optional<BankAccountEntity> findByAccountNumber(String accountNumber);
    List<BankAccountEntity> findByHolder_Id(Long holderId);
    List<BankAccountEntity> findByAccountStatus(String accountStatus);
    boolean existsByAccountNumber(String accountNumber);
}
