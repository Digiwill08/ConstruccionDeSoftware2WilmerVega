package gestiondeunbanco.wilmervega.domain.repository;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaturalClientRepository extends JpaRepository<NaturalClient, Long> {
    Optional<NaturalClient> findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
}