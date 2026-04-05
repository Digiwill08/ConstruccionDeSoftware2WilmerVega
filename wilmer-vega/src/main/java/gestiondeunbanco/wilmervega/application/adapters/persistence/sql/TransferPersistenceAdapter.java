package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.TransferRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.TransferEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        TransferEntity entity = toEntity(transfer);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private TransferEntity toEntity(Transfer model) {
        TransferEntity entity = new TransferEntity();
        entity.setTransferId(model.getTransferId());
        if (model.getSourceAccount() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity o = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity();
            o.setId(model.getSourceAccount().getId());
            entity.setSourceAccount(o);
        }
        if (model.getDestinationAccount() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity d = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity();
            d.setId(model.getDestinationAccount().getId());
            entity.setDestinationAccount(d);
        }
        entity.setAmount(model.getAmount());
        entity.setCreationDateTime(model.getCreationDateTime());
        entity.setApprovalDateTime(model.getApprovalDateTime());
        if (model.getTransferStatus() != null) entity.setTransferStatus(model.getTransferStatus().name());
        return entity;
    }

    private Transfer toModel(TransferEntity entity) {
        Transfer model = new Transfer();
        model.setTransferId(entity.getTransferId());
        
        if (entity.getSourceAccount() != null) {
            gestiondeunbanco.wilmervega.domain.models.BankAccount o = new gestiondeunbanco.wilmervega.domain.models.BankAccount();
            o.setId(entity.getSourceAccount().getId());
            model.setSourceAccount(o);
        }
        if (entity.getDestinationAccount() != null) {
            gestiondeunbanco.wilmervega.domain.models.BankAccount d = new gestiondeunbanco.wilmervega.domain.models.BankAccount();
            d.setId(entity.getDestinationAccount().getId());
            model.setDestinationAccount(d);
        }
        
        model.setAmount(entity.getAmount());
        model.setCreationDateTime(entity.getCreationDateTime());
        model.setApprovalDateTime(entity.getApprovalDateTime());
        if (entity.getTransferStatus() != null) model.setTransferStatus(TransferStatus.valueOf(entity.getTransferStatus()));
        return model;
    }
}
