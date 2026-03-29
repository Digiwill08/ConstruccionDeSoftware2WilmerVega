package gestiondeunbanco.wilmervega.infrastructure.adapters.repository;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
