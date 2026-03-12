package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyClientRepository extends MongoRepository<CompanyClient, Long> {
}
