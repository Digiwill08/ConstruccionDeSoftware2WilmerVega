package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Domain repository for User entity (Aggregate Root).
 * Defines the contract for user persistence.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByIdentificationId(String identificationId);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByIdentificationId(String identificationId);
    
    boolean existsByEmail(String email);
}
