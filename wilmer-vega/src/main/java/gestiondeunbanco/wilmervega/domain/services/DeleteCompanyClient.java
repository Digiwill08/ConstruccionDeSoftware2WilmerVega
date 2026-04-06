package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

public class DeleteCompanyClient {

    private final CompanyClientPort companyClientPort;

    public DeleteCompanyClient(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public void deleteById(Long id) {
        companyClientPort.deleteById(id);
    }
}
