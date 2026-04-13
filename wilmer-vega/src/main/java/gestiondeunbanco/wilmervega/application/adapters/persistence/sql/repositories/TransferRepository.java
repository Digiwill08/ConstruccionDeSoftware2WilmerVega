package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findByTransferStatus(String transferStatus);

    @Query("SELECT t FROM TransferEntity t WHERE t.transferStatus = 'AWAITING_APPROVAL' AND t.creationDateTime <= :cutoffTime")
    List<TransferEntity> findAwaitingApprovalOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

    List<TransferEntity> findBySourceAccount_AccountNumber(String accountNumber);
}
