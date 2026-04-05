package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.NaturalClientRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaturalClientPersistenceAdapter implements NaturalClientPort {

    private final NaturalClientRepository repository;

    @Override
    public List<NaturalClient> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<NaturalClient> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return repository.findAll().stream().filter(r->r.getDocumentNumber().equals(documentNumber)).findFirst().map(this::toModel);
    }
    
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.findAll().stream().anyMatch(r->r.getDocumentNumber().equals(documentNumber));
    }

    @Override
    public NaturalClient save(NaturalClient naturalClient) {
        NaturalClientEntity entity = toEntity(naturalClient);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private NaturalClientEntity toEntity(NaturalClient model) {
        NaturalClientEntity entity = new NaturalClientEntity();
        entity.setId(model.getId());
        entity.setDocumentNumber(model.getDocumentNumber());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setAddress(model.getAddress());
        entity.setFullName(model.getFullName());
        entity.setBirthDate(model.getBirthDate());
        if (model.getRole() != null) entity.setRole(model.getRole().name());
        return entity;
    }

    private NaturalClient toModel(NaturalClientEntity entity) {
        NaturalClient model = new NaturalClient();
        model.setId(entity.getId());
        model.setDocumentNumber(entity.getDocumentNumber());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setAddress(entity.getAddress());
        model.setFullName(entity.getFullName());
        model.setBirthDate(entity.getBirthDate());
        if (entity.getRole() != null) model.setRole(SystemRole.valueOf(entity.getRole()));
        return model;
    }
}
