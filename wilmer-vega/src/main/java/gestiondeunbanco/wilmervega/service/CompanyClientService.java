package gestiondeunbanco.wilmervega.service;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.repository.CompanyClientRepository;
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

    public Optional<CompanyClient> findById(Long id) {
        return companyClientRepository.findById(id);
    }

    public Optional<CompanyClient> findByTaxId(String taxId) {
        return companyClientRepository.findByTaxId(taxId);
    }

    public CompanyClient save(CompanyClient companyClient) {
        String taxId = companyClient.getTaxId();
        if (taxId == null || taxId.isBlank()) {
            throw new IllegalArgumentException("Tax ID is required");
        }

        if (companyClientRepository.existsByTaxId(taxId)) {
            throw new IllegalArgumentException("Tax ID already exists");
        }

        return companyClientRepository.save(companyClient);
    }

    public void deleteById(Long id) {
        companyClientRepository.deleteById(id);
    }
}
