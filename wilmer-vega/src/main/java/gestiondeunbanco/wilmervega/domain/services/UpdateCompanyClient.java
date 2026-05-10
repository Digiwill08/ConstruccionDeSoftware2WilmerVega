package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

public class UpdateCompanyClient {

    private final CompanyClientPort companyClientPort;

    public UpdateCompanyClient(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public CompanyClient update(CompanyClient companyClient) {
        if (companyClient == null || companyClient.getId() == null) {
            throw new IllegalArgumentException("CompanyClient or ID cannot be null for update");
        }
        if (companyClient.getDocumentNumber() == null || companyClient.getDocumentNumber().isBlank()) {
            throw new IllegalArgumentException("Company document number is required");
        }
        if (!companyClient.getDocumentNumber().matches("^\\d+$")) {
            throw new IllegalArgumentException("Company document number must be strictly numeric");
        }
        if (companyClientPort.findById(companyClient.getId()).isEmpty()) {
            throw new NotFoundException("CompanyClient not found for update");
        }
        return companyClientPort.save(companyClient);
    }
}
