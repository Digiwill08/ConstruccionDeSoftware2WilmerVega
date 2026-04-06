package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

public class DeleteNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public DeleteNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public void deleteById(Long id) {
        naturalClientPort.deleteById(id);
    }
}
