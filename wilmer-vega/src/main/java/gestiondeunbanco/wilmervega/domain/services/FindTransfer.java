package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

import java.util.List;
import java.util.Optional;

public class FindTransfer {

    private final TransferPort transferPort;

    public FindTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public List<Transfer> findAll() {
        return transferPort.findAll();
    }

    public Optional<Transfer> findById(Long id) {
        return transferPort.findById(id);
    }
}
