package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.CompanyClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyClientPersistenceAdapter implements CompanyClientPort {

    private final CompanyClientRepository companyClientRepository;

    @Override
    public List<CompanyClient> findAll() {
        return companyClientRepository.findAll();
    }

    @Override
    public Optional<CompanyClient> findById(Long id) {
        return companyClientRepository.findById(id);
    }

    @Override
    public CompanyClient save(CompanyClient companyClient) {
        return companyClientRepository.save(companyClient);
    }

    @Override
    public void deleteById(Long id) {
        companyClientRepository.deleteById(id);
    }
}
