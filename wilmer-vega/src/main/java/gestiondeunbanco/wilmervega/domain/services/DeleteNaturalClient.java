package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

public class DeleteNaturalClient {

    private final NaturalClientPort naturalClientPort;

    public DeleteNaturalClient(NaturalClientPort naturalClientPort) {
        this.naturalClientPort = naturalClientPort;
    }

    public void deleteById(Long id) {
        if (naturalClientPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: NaturalClient not found with ID " + id);
        }
        naturalClientPort.deleteById(id);
    }
}
