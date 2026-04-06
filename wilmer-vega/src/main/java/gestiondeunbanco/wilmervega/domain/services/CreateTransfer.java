package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

public class CreateTransfer {

    private final TransferPort transferPort;

    public CreateTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public Transfer save(Transfer transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer cannot be null");
        }
        return transferPort.save(transfer);
    }
}
