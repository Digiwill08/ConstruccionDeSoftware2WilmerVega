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

    public CompanyClient createCompanyClient(CompanyClient companyClient) {
        return companyClientRepository.save(companyClient);
    }

    public Optional<CompanyClient> getCompanyClientById(Long id) {
        return companyClientRepository.findById(id);
    }

    public List<CompanyClient> getAllCompanyClients() {
        return companyClientRepository.findAll();
    }

    public CompanyClient updateCompanyClient(CompanyClient companyClient) {
        return companyClientRepository.save(companyClient);
    }

    public void deleteCompanyClient(Long id) {
        companyClientRepository.deleteById(id);
    }
}
