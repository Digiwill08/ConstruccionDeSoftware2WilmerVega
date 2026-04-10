package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

public class UpdateNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public UpdateNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public NaturalClient update(NaturalClient naturalClient) {
        if (naturalClient == null || naturalClient.getId() == null) {
            throw new IllegalArgumentException("NaturalClient or ID cannot be null for update");
        }
        if (naturalClientPort.findById(naturalClient.getId()).isEmpty()) {
            throw new NotFoundException("NaturalClient not found for update");
        }
        return naturalClientPort.save(naturalClient);
    }
}
