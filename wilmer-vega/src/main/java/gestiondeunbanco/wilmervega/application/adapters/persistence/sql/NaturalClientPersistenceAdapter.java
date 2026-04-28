package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.NaturalClientRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers.NaturalClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NaturalClientPersistenceAdapter implements NaturalClientPort {

    private final NaturalClientRepository repository;

    @Override
    public List<NaturalClient> findAll() {
        return repository.findAll().stream().map(NaturalClientMapper::toModel).toList();
    }

    @Override
    public Optional<NaturalClient> findById(Long id) {
        return repository.findById(id).map(NaturalClientMapper::toModel);
    }

    @Override
    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber).map(NaturalClientMapper::toModel);
    }
    
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public NaturalClient save(NaturalClient naturalClient) {
        return NaturalClientMapper.toModel(repository.save(NaturalClientMapper.toEntity(naturalClient)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
