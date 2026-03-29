package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransferDomainService {

    private final TransferPort transferPort;

    public TransferDomainService(TransferPort transferPort) {
        this.transferPort = transferPort;
    }

    public List<Transfer> findAll() {
        return transferPort.findAll();
    }

    public Optional<Transfer> findById(Long id) {
        return transferPort.findById(id);
    }

    public Transfer save(Transfer transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer cannot be null");
        }
        if (transfer.getAmount() != null && transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        return transferPort.save(transfer);
    }

    public void deleteById(Long id) {
        transferPort.deleteById(id);
    }
}
