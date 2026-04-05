package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
