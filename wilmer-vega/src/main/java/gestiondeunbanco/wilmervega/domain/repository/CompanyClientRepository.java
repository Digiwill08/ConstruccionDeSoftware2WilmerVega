package gestiondeunbanco.wilmervega.domain.repository;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyClientRepository extends JpaRepository<CompanyClient, Long> {
}
