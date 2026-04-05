package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AccountType;
import gestiondeunbanco.wilmervega.domain.models.Currency;
import gestiondeunbanco.wilmervega.domain.models.Client;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.BankAccountRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository repository;

    @Override
    public List<BankAccount> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).map(this::toModel);
    }

    @Override
    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return repository.findAll().stream().filter(r -> r.getAccountStatus() != null && r.getAccountStatus().equals(status.name())).map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccountEntity entity = toEntity(bankAccount);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private BankAccountEntity toEntity(BankAccount model) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(model.getId());
        entity.setAccountNumber(model.getAccountNumber());
        entity.setAccountType(model.getAccountType() != null ? model.getAccountType().name() : null);
        entity.setCurrentBalance(model.getCurrentBalance());
        entity.setCurrency(model.getCurrency() != null ? model.getCurrency().name() : null);
        entity.setAccountStatus(model.getAccountStatus() != null ? model.getAccountStatus().name() : null);
        entity.setOpeningDate(model.getOpeningDate());
        if (model.getHolder() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity clientEntity = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity();
            clientEntity.setId(model.getHolder().getId());
            entity.setHolder(clientEntity);
        }
        return entity;
    }

    private BankAccount toModel(BankAccountEntity entity) {
        BankAccount model = new BankAccount();
        model.setId(entity.getId());
        model.setAccountNumber(entity.getAccountNumber());
        if (entity.getAccountType() != null) model.setAccountType(AccountType.valueOf(entity.getAccountType()));
        model.setCurrentBalance(entity.getCurrentBalance());
        if (entity.getCurrency() != null) model.setCurrency(Currency.valueOf(entity.getCurrency()));
        if (entity.getAccountStatus() != null) model.setAccountStatus(AccountStatus.valueOf(entity.getAccountStatus()));
        model.setOpeningDate(entity.getOpeningDate());
        if (entity.getHolder() != null) {
            gestiondeunbanco.wilmervega.domain.models.NaturalClient c = new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
            c.setId(entity.getHolder().getId());
            model.setHolder(c);
        }
        return model;
    }
}
