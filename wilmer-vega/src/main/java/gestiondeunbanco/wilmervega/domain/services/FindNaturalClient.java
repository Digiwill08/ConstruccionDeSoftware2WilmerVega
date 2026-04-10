package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

import java.util.List;

public class FindNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public FindNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public List<NaturalClient> findAll() {
        return naturalClientPort.findAll();
    }

    public NaturalClient findById(Long id) {
        return naturalClientPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Natural Client not found with ID: " + id));
    }

    public NaturalClient findByDocumentNumber(String documentNumber) {
        return naturalClientPort.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NotFoundException("Natural Client not found with document: " + documentNumber));
    }
}
