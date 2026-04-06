package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

import java.util.List;
import java.util.Optional;

public class FindNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public FindNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public List<NaturalClient> findAll() {
        return naturalClientPort.findAll();
    }

    public Optional<NaturalClient> findById(Long id) {
        return naturalClientPort.findById(id);
    }

    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return naturalClientPort.findByDocumentNumber(documentNumber);
    }
}
