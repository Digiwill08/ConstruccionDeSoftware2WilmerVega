package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferPersistenceAdapter implements TransferPort {

    private final TransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public Transfer save(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }
}
