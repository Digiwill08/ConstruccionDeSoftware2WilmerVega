package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransferPort {
    List<Transfer> findAll();
    Optional<Transfer> findById(Long id);
    Transfer save(Transfer transfer);
    void deleteById(Long id);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findAwaitingApprovalOlderThan(LocalDateTime cutoffTime);
    List<Transfer> findBySourceAccountNumber(String accountNumber);
}
