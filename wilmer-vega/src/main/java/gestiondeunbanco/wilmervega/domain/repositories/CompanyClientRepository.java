package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.CompanyClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Domain repository for CompanyClient entity.
 * Defines persistence operations for company clients.
 */
@Repository
public interface CompanyClientRepository extends JpaRepository<CompanyClient, Long> {
    
    Optional<CompanyClient> findByTaxId(String taxId);
    
    boolean existsByTaxId(String taxId);
}
