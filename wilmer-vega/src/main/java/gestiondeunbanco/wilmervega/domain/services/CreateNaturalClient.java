package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

import java.time.LocalDate;
import java.time.Period;

public class CreateNaturalClient {

    private final NaturalClientPort naturalClientPort;
    private final CompanyClientPort companyClientPort;

    public CreateNaturalClient(NaturalClientPort naturalClientPort, CompanyClientPort companyClientPort) {
        this.naturalClientPort = naturalClientPort;
        this.companyClientPort = companyClientPort;
    }

    public NaturalClient save(NaturalClient naturalClient) {
        if (naturalClient == null) {
            throw new IllegalArgumentException("NaturalClient cannot be null");
        }
        if (naturalClient.getFullName() == null || naturalClient.getFullName().isBlank()) {
            throw new IllegalArgumentException("Natural client full name is required");
        }
        if (naturalClient.getBirthDate() == null) {
            throw new IllegalArgumentException("Natural client birth date is required");
        }
        if (Period.between(naturalClient.getBirthDate(), LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Natural client must be at least 18 years old");
        }
        if (naturalClient.getEmail() == null || naturalClient.getEmail().isBlank() || !naturalClient.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (naturalClient.getPhone() == null || naturalClient.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (naturalClient.getAddress() == null || naturalClient.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (naturalClient.getRole() == null) {
            throw new IllegalArgumentException("Natural client role is required");
        }
        if (naturalClient.getDocumentNumber() != null && naturalClientPort.existsByDocumentNumber(naturalClient.getDocumentNumber())) {
            throw new IllegalArgumentException("A natural client with this document number already exists");
        }
        if (naturalClient.getDocumentNumber() != null && companyClientPort.existsByDocumentNumber(naturalClient.getDocumentNumber())) {
            throw new IllegalArgumentException("A company client with this document number already exists");
        }
        return naturalClientPort.save(naturalClient);
    }
}
