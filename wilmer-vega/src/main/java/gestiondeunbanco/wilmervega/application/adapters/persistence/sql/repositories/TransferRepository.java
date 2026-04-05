package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
}
