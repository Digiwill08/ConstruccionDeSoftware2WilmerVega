package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

import java.util.List;

public class FindTransfer {

    private final TransferPort transferPort;

    public FindTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public List<Transfer> findAll() {
        return transferPort.findAll();
    }

    public Transfer findById(Long id) {
        return transferPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + id));
    }

    public List<Transfer> findByStatus(TransferStatus status) {
        return transferPort.findByStatus(status);
    }
}
