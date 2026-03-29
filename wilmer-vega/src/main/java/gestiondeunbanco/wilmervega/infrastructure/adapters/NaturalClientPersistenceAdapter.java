package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.NaturalClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NaturalClientPersistenceAdapter implements NaturalClientPort {

    private final NaturalClientRepository naturalClientRepository;

    @Override
    public List<NaturalClient> findAll() {
        return naturalClientRepository.findAll();
    }

    @Override
    public Optional<NaturalClient> findById(Long id) {
        return naturalClientRepository.findById(id);
    }

    @Override
    public NaturalClient save(NaturalClient naturalClient) {
        return naturalClientRepository.save(naturalClient);
    }

    @Override
    public void deleteById(Long id) {
        naturalClientRepository.deleteById(id);
    }

    @Override
    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return naturalClientRepository.findByDocumentNumber(documentNumber);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return naturalClientRepository.existsByDocumentNumber(documentNumber);
    }
}
