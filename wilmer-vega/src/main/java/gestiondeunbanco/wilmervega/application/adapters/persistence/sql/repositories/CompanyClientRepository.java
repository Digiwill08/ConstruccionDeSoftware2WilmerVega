package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyClientRepository extends JpaRepository<CompanyClientEntity, Long> {
	Optional<CompanyClientEntity> findByDocumentNumber(String documentNumber);
	boolean existsByDocumentNumber(String documentNumber);
}
