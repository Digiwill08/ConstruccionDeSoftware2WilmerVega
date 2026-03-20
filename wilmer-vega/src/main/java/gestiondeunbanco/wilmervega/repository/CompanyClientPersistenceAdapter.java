package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyClientPersistenceAdapter implements CompanyClientPort {
    private final CompanyClientRepository repository;

    @Override public CompanyClient save(CompanyClient companyClient) { return repository.save(companyClient); }
    @Override public Optional<CompanyClient> findById(Long id) { return repository.findById(id); }
    @Override public List<CompanyClient> findAll() { return repository.findAll(); }
    @Override public void deleteById(Long id) { repository.deleteById(id); }
}
