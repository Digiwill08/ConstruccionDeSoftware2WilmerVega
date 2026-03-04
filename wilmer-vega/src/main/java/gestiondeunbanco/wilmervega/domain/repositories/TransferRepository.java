package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.Transfer;
import gestiondeunbanco.wilmervega.domain.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Domain repository for Transfer entity (Aggregate Root).
 * Defines persistence operations for transfers.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    
    List<Transfer> findBySourceAccount(String sourceAccount);
    
    List<Transfer> findByDestinationAccount(String destinationAccount);
    
    List<Transfer> findByTransferStatus(TransferStatus transferStatus);
    
    List<Transfer> findByCreatorUserId(Long creatorUserId);
}
