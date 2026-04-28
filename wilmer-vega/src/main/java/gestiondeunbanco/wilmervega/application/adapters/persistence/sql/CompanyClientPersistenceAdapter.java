package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.CompanyClientRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers.CompanyClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyClientPersistenceAdapter implements CompanyClientPort {

    private final CompanyClientRepository repository;

    @Override
    public List<CompanyClient> findAll() {
        return repository.findAll().stream().map(CompanyClientMapper::toModel).toList();
    }

    @Override
    public Optional<CompanyClient> findById(Long id) {
        return repository.findById(id).map(CompanyClientMapper::toModel);
    }

    @Override
    public Optional<CompanyClient> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber).map(CompanyClientMapper::toModel);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public CompanyClient save(CompanyClient companyClient) {
        return CompanyClientMapper.toModel(repository.save(CompanyClientMapper.toEntity(companyClient)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
