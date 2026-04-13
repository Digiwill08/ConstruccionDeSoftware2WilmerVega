package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.TransferRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.TransferEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransferPersistenceAdapter implements TransferPort {

    private final TransferRepository repository;

    @Override
    public List<Transfer> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Transfer save(Transfer transfer) {
        return toModel(repository.save(toEntity(transfer)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return repository.findByTransferStatus(status.name()).stream()
                .map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findAwaitingApprovalOlderThan(LocalDateTime cutoffTime) {
        return repository.findAwaitingApprovalOlderThan(cutoffTime).stream()
                .map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findBySourceAccountNumber(String accountNumber) {
        return repository.findBySourceAccount_AccountNumber(accountNumber).stream()
                .map(this::toModel).collect(Collectors.toList());
    }

    private TransferEntity toEntity(Transfer model) {
        TransferEntity entity = new TransferEntity();
        entity.setTransferId(model.getTransferId());
        if (model.getSourceAccount() != null && model.getSourceAccount().getId() != null) {
            BankAccountEntity o = new BankAccountEntity();
            o.setId(model.getSourceAccount().getId());
            entity.setSourceAccount(o);
        }
        if (model.getDestinationAccount() != null && model.getDestinationAccount().getId() != null) {
            BankAccountEntity d = new BankAccountEntity();
            d.setId(model.getDestinationAccount().getId());
            entity.setDestinationAccount(d);
        }
        entity.setAmount(model.getAmount());
        entity.setCreationDateTime(model.getCreationDateTime());
        entity.setApprovalDateTime(model.getApprovalDateTime());
        if (model.getTransferStatus() != null) entity.setTransferStatus(model.getTransferStatus().name());
        entity.setCreatorUserId(model.getCreatorUserId());
        entity.setApproverUserId(model.getApproverUserId());
        return entity;
    }

    private Transfer toModel(TransferEntity entity) {
        Transfer model = new Transfer();
        model.setTransferId(entity.getTransferId());
        if (entity.getSourceAccount() != null) {
            gestiondeunbanco.wilmervega.domain.models.BankAccount o = new gestiondeunbanco.wilmervega.domain.models.BankAccount();
            o.setId(entity.getSourceAccount().getId());
            o.setAccountNumber(entity.getSourceAccount().getAccountNumber());
            o.setCurrentBalance(entity.getSourceAccount().getCurrentBalance());
            if (entity.getSourceAccount().getAccountStatus() != null) {
                o.setAccountStatus(gestiondeunbanco.wilmervega.domain.models.AccountStatus
                        .valueOf(entity.getSourceAccount().getAccountStatus()));
            }
            model.setSourceAccount(o);
        }
        if (entity.getDestinationAccount() != null) {
            gestiondeunbanco.wilmervega.domain.models.BankAccount d = new gestiondeunbanco.wilmervega.domain.models.BankAccount();
            d.setId(entity.getDestinationAccount().getId());
            d.setAccountNumber(entity.getDestinationAccount().getAccountNumber());
            model.setDestinationAccount(d);
        }
        model.setAmount(entity.getAmount());
        model.setCreationDateTime(entity.getCreationDateTime());
        model.setApprovalDateTime(entity.getApprovalDateTime());
        if (entity.getTransferStatus() != null) model.setTransferStatus(TransferStatus.valueOf(entity.getTransferStatus()));
        model.setCreatorUserId(entity.getCreatorUserId());
        model.setApproverUserId(entity.getApproverUserId());
        return model;
    }
}
