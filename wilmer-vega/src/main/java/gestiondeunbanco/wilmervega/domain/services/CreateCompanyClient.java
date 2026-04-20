package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

public class CreateCompanyClient {

    private final CompanyClientPort companyClientPort;
    private final NaturalClientPort naturalClientPort;

    public CreateCompanyClient(CompanyClientPort companyClientPort, NaturalClientPort naturalClientPort) {
        this.companyClientPort = companyClientPort;
        this.naturalClientPort = naturalClientPort;
    }

    public CompanyClient save(CompanyClient companyClient) {
        if (companyClient == null) {
            throw new IllegalArgumentException("CompanyClient cannot be null");
        }
        if (companyClient.getBusinessName() == null || companyClient.getBusinessName().isBlank()) {
            throw new IllegalArgumentException("Company business name is required");
        }
        if (companyClient.getEmail() == null || companyClient.getEmail().isBlank() || !companyClient.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (companyClient.getPhone() == null || companyClient.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (companyClient.getAddress() == null || companyClient.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (companyClient.getDocumentNumber() != null && companyClientPort.existsByDocumentNumber(companyClient.getDocumentNumber())) {
            throw new IllegalArgumentException("A company client with this document number already exists");
        }
        if (companyClient.getDocumentNumber() != null && naturalClientPort.existsByDocumentNumber(companyClient.getDocumentNumber())) {
            throw new IllegalArgumentException("A natural client with this document number already exists");
        }
        if (companyClient.getLegalRepresentative() == null || companyClient.getLegalRepresentative().getId() == null) {
            throw new IllegalArgumentException("Legal representative is required");
        }
        naturalClientPort.findById(companyClient.getLegalRepresentative().getId())
                .orElseThrow(() -> new IllegalArgumentException("Legal representative not found"));
        return companyClientPort.save(companyClient);
    }
}
