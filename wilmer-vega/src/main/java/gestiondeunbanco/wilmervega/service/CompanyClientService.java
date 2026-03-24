package gestiondeunbanco.wilmervega.service;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.repository.CompanyClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyClientService {

    private final CompanyClientRepository companyClientRepository;

    public List<CompanyClient> findAll() {
        return companyClientRepository.findAll();
    }

    public void deleteById(Long id) {
        companyClientRepository.deleteById(id);
    }

    public Optional<CompanyClient> findById(Long id) {
        return companyClientRepository.findById(id);
    }

    public CompanyClient save(CompanyClient companyClient) {
        return companyClientRepository.save(companyClient);
    }
}
