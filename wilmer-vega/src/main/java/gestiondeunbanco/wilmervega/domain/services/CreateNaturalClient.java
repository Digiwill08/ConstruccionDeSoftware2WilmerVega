package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

public class CreateNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public CreateNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
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
}
