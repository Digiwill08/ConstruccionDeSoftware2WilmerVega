package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AccountType;
import gestiondeunbanco.wilmervega.domain.models.Currency;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.BankAccountRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository repository;

    @Override
    public List<BankAccount> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public List<BankAccount> findByHolderId(Long holderId) {
        return repository.findByHolder_Id(holderId).stream().map(this::toModel).toList();
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).map(this::toModel);
    }

    @Override
    public List<BankAccount> findByAccountStatus(AccountStatus status) {
        return repository.findByAccountStatus(status.name()).stream().map(this::toModel).toList();
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return toModel(repository.save(toEntity(bankAccount)));
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
            ClientEntity clientEntity = new ClientEntity();
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

        // Reconstruct holder respecting real type (JOINED inheritance)
        if (entity.getHolder() instanceof gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity companyEntity) {
            gestiondeunbanco.wilmervega.domain.models.CompanyClient company =
                    new gestiondeunbanco.wilmervega.domain.models.CompanyClient();
            company.setId(companyEntity.getId());
            company.setDocumentNumber(companyEntity.getDocumentNumber());
            company.setEmail(companyEntity.getEmail());
            company.setPhone(companyEntity.getPhone());
            company.setAddress(companyEntity.getAddress());
            company.setBusinessName(companyEntity.getBusinessName());
            model.setHolder(company);
        } else if (entity.getHolder() != null) {
            gestiondeunbanco.wilmervega.domain.models.NaturalClient natural =
                    new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
            natural.setId(entity.getHolder().getId());
            natural.setDocumentNumber(entity.getHolder().getDocumentNumber());
            natural.setEmail(entity.getHolder().getEmail());
            natural.setPhone(entity.getHolder().getPhone());
            natural.setAddress(entity.getHolder().getAddress());
            if (entity.getHolder() instanceof gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity naturalEntity) {
                natural.setFullName(naturalEntity.getFullName());
                natural.setBirthDate(naturalEntity.getBirthDate());
                if (naturalEntity.getRole() != null) {
                    try {
                        natural.setRole(gestiondeunbanco.wilmervega.domain.models.SystemRole.valueOf(naturalEntity.getRole()));
                    } catch (IllegalArgumentException ignored) { }
                }
            }
            model.setHolder(natural);
        }

        return model;
    }
}
