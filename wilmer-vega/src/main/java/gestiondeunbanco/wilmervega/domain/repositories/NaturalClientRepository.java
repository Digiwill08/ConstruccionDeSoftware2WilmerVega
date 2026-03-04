package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.NaturalClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Domain repository for NaturalClient entity.
 * Defines persistence operations for natural person clients.
 */
@Repository
public interface NaturalClientRepository extends JpaRepository<NaturalClient, Long> {
    
    Optional<NaturalClient> findByIdentificationNumber(String identificationNumber);
    
    Optional<NaturalClient> findByEmail(String email);
    
    boolean existsByIdentificationNumber(String identificationNumber);
    
    boolean existsByEmail(String email);
}
