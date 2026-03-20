package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyClientService {

    private final CompanyClientPort CompanyClientPort;

    public CompanyClient createCompanyClient(CompanyClient companyClient) {
        return CompanyClientPort.save(companyClient);
    }

    public Optional<CompanyClient> getCompanyClientById(Long id) {
        return CompanyClientPort.findById(id);
    }

    public List<CompanyClient> getAllCompanyClients() {
        return CompanyClientPort.findAll();
    }

    public CompanyClient updateCompanyClient(CompanyClient companyClient) {
        return CompanyClientPort.save(companyClient);
    }

    public void deleteCompanyClient(Long id) {
        CompanyClientPort.deleteById(id);
    }

    public void deleteById(Long id) {
        CompanyClientPort.deleteById(id);
    }

    public Optional<CompanyClient> findById(Long id) {
        return CompanyClientPort.findById(id);
    }

    public CompanyClient save(CompanyClient companyClient) {
        return CompanyClientPort.save(companyClient);
    }
}
