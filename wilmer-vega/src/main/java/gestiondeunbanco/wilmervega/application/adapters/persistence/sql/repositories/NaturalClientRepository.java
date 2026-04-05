package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaturalClientRepository extends JpaRepository<NaturalClientEntity, Long> {
    Optional<NaturalClientEntity> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);
}
