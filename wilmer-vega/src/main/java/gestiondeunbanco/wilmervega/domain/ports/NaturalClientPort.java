package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;

import java.util.List;
import java.util.Optional;

public interface NaturalClientPort {
    List<NaturalClient> findAll();
    Optional<NaturalClient> findById(Long id);
    NaturalClient save(NaturalClient naturalClient);
    void deleteById(Long id);
    Optional<NaturalClient> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);
}
