package gestiondeunbanco.wilmervega.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;

@Repository // <-- ASEGÚRATE DE QUE TENGA ESTO
public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
    // Tu código actual...
}
