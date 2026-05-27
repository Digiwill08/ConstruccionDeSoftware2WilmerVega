package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanType;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.LoanRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.LoanEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Loan save(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        return toModel(repository.save(toEntity(loan)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        return repository.findByLoanStatus(status.name()).stream()
                .map(this::toModel).toList();
    }

    @Override
    public List<Loan> findByClientDocument(String documentNumber) {
        return repository.findByClientApplicant_DocumentNumber(documentNumber).stream()
                .map(this::toModel).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private LoanEntity toEntity(Loan model) {
        LoanEntity entity = new LoanEntity();
        entity.setLoanId(model.getLoanId());
        if (model.getLoanType() != null) entity.setLoanType(model.getLoanType().name());
        entity.setRequestedAmount(model.getRequestedAmount());
        entity.setApprovedAmount(model.getApprovedAmount());
        entity.setInterestRate(model.getInterestRate());
        entity.setTermInMonths(model.getTermInMonths());
        if (model.getLoanStatus() != null) entity.setLoanStatus(model.getLoanStatus().name());
        entity.setApprovalDate(model.getApprovalDate());
        entity.setDisbursementDate(model.getDisbursementDate());
        entity.setApprovedByUserId(model.getApprovedByUserId());
        if (model.getClientApplicant() != null && model.getClientApplicant().getId() != null) {
            entity.setClientApplicant(entityManager.getReference(ClientEntity.class, model.getClientApplicant().getId()));
        }
        if (model.getDisbursementAccount() != null && model.getDisbursementAccount().getId() != null) {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(model.getDisbursementAccount().getId());
            entity.setDisbursementAccount(ba);
        }
        return entity;
    }

    private Loan toModel(LoanEntity entity) {
        Loan model = new Loan();
        model.setLoanId(entity.getLoanId());
        if (entity.getLoanType() != null) model.setLoanType(LoanType.valueOf(entity.getLoanType()));
        model.setRequestedAmount(entity.getRequestedAmount());
        model.setApprovedAmount(entity.getApprovedAmount());
        model.setInterestRate(entity.getInterestRate());
        model.setTermInMonths(entity.getTermInMonths());
        if (entity.getLoanStatus() != null) model.setLoanStatus(LoanStatus.valueOf(entity.getLoanStatus()));
        model.setApprovalDate(entity.getApprovalDate());
        model.setDisbursementDate(entity.getDisbursementDate());
        model.setApprovedByUserId(entity.getApprovedByUserId());

        // Reconstruct clientApplicant from stored entity
        if (entity.getClientApplicant() != null) {
            ClientEntity clientEntity = entity.getClientApplicant();
            if (clientEntity instanceof CompanyClientEntity companyEntity) {
                gestiondeunbanco.wilmervega.domain.models.CompanyClient applicant =
                        new gestiondeunbanco.wilmervega.domain.models.CompanyClient();
                applicant.setId(companyEntity.getId());
                applicant.setDocumentNumber(companyEntity.getDocumentNumber());
                applicant.setEmail(companyEntity.getEmail());
                applicant.setPhone(companyEntity.getPhone());
                applicant.setAddress(companyEntity.getAddress());
                applicant.setBusinessName(companyEntity.getBusinessName());
                model.setClientApplicant(applicant);
            } else if (clientEntity instanceof NaturalClientEntity naturalEntity) {
                gestiondeunbanco.wilmervega.domain.models.NaturalClient applicant =
                        new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
                applicant.setId(naturalEntity.getId());
                applicant.setDocumentNumber(naturalEntity.getDocumentNumber());
                applicant.setEmail(naturalEntity.getEmail());
                applicant.setPhone(naturalEntity.getPhone());
                applicant.setAddress(naturalEntity.getAddress());
                applicant.setFullName(naturalEntity.getFullName());
                applicant.setBirthDate(naturalEntity.getBirthDate());
                if (naturalEntity.getRole() != null) {
                    applicant.setRole(gestiondeunbanco.wilmervega.domain.models.SystemRole.valueOf(naturalEntity.getRole()));
                }
                model.setClientApplicant(applicant);
            } else {
                gestiondeunbanco.wilmervega.domain.models.NaturalClient applicant =
                        new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
                applicant.setId(clientEntity.getId());
                applicant.setDocumentNumber(clientEntity.getDocumentNumber());
                applicant.setEmail(clientEntity.getEmail());
                applicant.setPhone(clientEntity.getPhone());
                applicant.setAddress(clientEntity.getAddress());
                model.setClientApplicant(applicant);
            }
        }

        // Reconstruct disbursementAccount from stored entity
        if (entity.getDisbursementAccount() != null) {
            gestiondeunbanco.wilmervega.domain.models.BankAccount account =
                    new gestiondeunbanco.wilmervega.domain.models.BankAccount();
            account.setId(entity.getDisbursementAccount().getId());
            account.setAccountNumber(entity.getDisbursementAccount().getAccountNumber());
            account.setCurrentBalance(entity.getDisbursementAccount().getCurrentBalance());
            if (entity.getDisbursementAccount().getAccountStatus() != null) {
                account.setAccountStatus(
                    gestiondeunbanco.wilmervega.domain.models.AccountStatus
                        .valueOf(entity.getDisbursementAccount().getAccountStatus()));
            }
            model.setDisbursementAccount(account);
        }

        return model;
    }
}
