package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.services.CompanyClientDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyClientUseCase {

    private final CompanyClientDomainService companyClientDomainService;

    public CompanyClientUseCase(CompanyClientPort companyClientPort) {
        // Instanciamos el servicio de dominio puro inyectándole el puerto
        this.companyClientDomainService = new CompanyClientDomainService(companyClientPort);
    }

    public CompanyClient save(CompanyClient companyClient) {
        return companyClientDomainService.save(companyClient);
    }

    public List<CompanyClient> findAll() {
        return companyClientDomainService.findAll();
    }

    public Optional<CompanyClient> findById(Long id) {
        return companyClientDomainService.findById(id);
    }

    public void deleteById(Long id) {
        companyClientDomainService.deleteById(id);
    }
}
