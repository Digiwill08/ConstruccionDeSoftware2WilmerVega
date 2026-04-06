package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

public class DeleteTransfer {

    private final TransferPort transferPort;

    public DeleteTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public void deleteById(Long id) {
        transferPort.deleteById(id);
    }
}
