package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

public class DeleteTransfer {

    private final TransferPort transferPort;

    public DeleteTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public void deleteById(Long id) {
        if (transferPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: Transfer not found with ID " + id);
        }
        transferPort.deleteById(id);
    }
}
