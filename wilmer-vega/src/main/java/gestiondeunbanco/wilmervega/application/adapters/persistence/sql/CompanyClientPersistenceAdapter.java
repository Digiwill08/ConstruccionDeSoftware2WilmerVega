package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.CompanyClientRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompanyClientPersistenceAdapter implements CompanyClientPort {

    private final CompanyClientRepository repository;

    @Override
    public List<CompanyClient> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyClient> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public CompanyClient save(CompanyClient companyClient) {
        CompanyClientEntity entity = toEntity(companyClient);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private CompanyClientEntity toEntity(CompanyClient model) {
        CompanyClientEntity entity = new CompanyClientEntity();
        entity.setId(model.getId());
        entity.setDocumentNumber(model.getDocumentNumber());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setAddress(model.getAddress());
        entity.setBusinessName(model.getBusinessName());
        if (model.getLegalRepresentative() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity nr = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity();
            nr.setId(model.getLegalRepresentative().getId());
            entity.setLegalRepresentative(nr);
        }
        return entity;
    }

    private CompanyClient toModel(CompanyClientEntity entity) {
        CompanyClient model = new CompanyClient();
        model.setId(entity.getId());
        model.setDocumentNumber(entity.getDocumentNumber());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setAddress(entity.getAddress());
        model.setBusinessName(entity.getBusinessName());
        if (entity.getLegalRepresentative() != null) {
            gestiondeunbanco.wilmervega.domain.models.NaturalClient c = new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
            c.setId(entity.getLegalRepresentative().getId());
            model.setLegalRepresentative(c);
        }
        return model;
    }
}
