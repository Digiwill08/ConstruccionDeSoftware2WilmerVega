package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;

import java.util.List;
import java.util.Optional;

public interface CompanyClientPort {
    List<CompanyClient> findAll();
    Optional<CompanyClient> findById(Long id);
    CompanyClient save(CompanyClient companyClient);
    void deleteById(Long id);
}
