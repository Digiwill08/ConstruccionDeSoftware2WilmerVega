package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyClientRepository extends JpaRepository<CompanyClient, Long> {
    Optional<CompanyClient> findByTaxId(String taxId);
    boolean existsByTaxId(String taxId);
}