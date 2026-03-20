package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import java.util.List;
import java.util.Optional;

public interface CompanyClientPort {
    CompanyClient save(CompanyClient companyClient);
    Optional<CompanyClient> findById(Long id);
    List<CompanyClient> findAll();
    void deleteById(Long id);
}
