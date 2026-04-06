package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

import java.util.List;
import java.util.Optional;

public class FindCompanyClient {

    private final CompanyClientPort companyClientPort;

    public FindCompanyClient(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public List<CompanyClient> findAll() {
        return companyClientPort.findAll();
    }

    public Optional<CompanyClient> findById(Long id) {
        return companyClientPort.findById(id);
    }
}
