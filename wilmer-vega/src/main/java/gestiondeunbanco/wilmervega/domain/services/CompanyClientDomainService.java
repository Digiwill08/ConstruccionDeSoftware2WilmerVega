package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

import java.util.List;
import java.util.Optional;

public class CompanyClientDomainService {

    private final CompanyClientPort companyClientPort;

    public CompanyClientDomainService(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public CompanyClient save(CompanyClient companyClient) {
        if (companyClient == null) {
            throw new IllegalArgumentException("CompanyClient cannot be null");
        }
        return companyClientPort.save(companyClient);
    }

    public List<CompanyClient> findAll() {
        return companyClientPort.findAll();
    }

    public Optional<CompanyClient> findById(Long id) {
        return companyClientPort.findById(id);
    }

    public void deleteById(Long id) {
        companyClientPort.deleteById(id);
    }
}
