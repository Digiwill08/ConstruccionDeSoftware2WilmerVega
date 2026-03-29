package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.services.NaturalClientDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NaturalClientUseCase {

    private final NaturalClientDomainService naturalClientDomainService;

    public NaturalClientUseCase(NaturalClientPort naturalClientPort) {
        this.naturalClientDomainService = new NaturalClientDomainService(naturalClientPort);
    }

    public List<NaturalClient> findAll() {
        return naturalClientDomainService.findAll();
    }

    public Optional<NaturalClient> findById(Long id) {
        return naturalClientDomainService.findById(id);
    }

    public NaturalClient save(NaturalClient naturalClient) {
        return naturalClientDomainService.save(naturalClient);
    }

    public void deleteById(Long id) {
        naturalClientDomainService.deleteById(id);
    }

    public Optional<NaturalClient> findByDocumentNumber(String documentNumber) {
        return naturalClientDomainService.findByDocumentNumber(documentNumber);
    }
}
