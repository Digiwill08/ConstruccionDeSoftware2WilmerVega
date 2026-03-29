package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

import java.util.List;
import java.util.Optional;

public class NaturalClientDomainService {

    private final NaturalClientPort naturalClientPort;

    public NaturalClientDomainService(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public List<NaturalClient> findAll() {
        return naturalClientPort.findAll();
    }

    public Optional<NaturalClient> findById(Long id) {
        return naturalClientPort.findById(id);
    }

    public NaturalClient save(NaturalClient naturalClient) {
        if (naturalClient == null) {
            throw new IllegalArgumentException("NaturalClient cannot be null");
        }
        if (naturalClient.getDocumentNumber() != null && naturalClientPort.existsByDocumentNumber(naturalClient.getDocumentNumber())) {
            throw new IllegalArgumentException("A natural client with this document number already exists");
        }
        return naturalClientPort.save(naturalClient);
    }

    public void deleteById(Long id) {
        naturalClientPort.deleteById(id);
    }

    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return naturalClientPort.findByDocumentNumber(documentNumber);
    }
}
