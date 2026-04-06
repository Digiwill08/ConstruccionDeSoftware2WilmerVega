package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

public class CreateCompanyClient {

    private final CompanyClientPort companyClientPort;

    public CreateCompanyClient(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public CompanyClient save(CompanyClient companyClient) {
        if (companyClient == null) {
            throw new IllegalArgumentException("CompanyClient cannot be null");
        }
        return companyClientPort.save(companyClient);
    }
}
