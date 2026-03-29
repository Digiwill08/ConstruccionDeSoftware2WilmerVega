package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import gestiondeunbanco.wilmervega.domain.services.TransferDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferUseCase {

    private final TransferDomainService transferDomainService;

    public TransferUseCase(TransferPort transferPort) {
        this.transferDomainService = new TransferDomainService(transferPort);
    }

    public List<Transfer> findAll() {
        return transferDomainService.findAll();
    }

    public Optional<Transfer> findById(Long id) {
        return transferDomainService.findById(id);
    }

    public Transfer save(Transfer transfer) {
        return transferDomainService.save(transfer);
    }

    public void deleteById(Long id) {
        transferDomainService.deleteById(id);
    }
}
