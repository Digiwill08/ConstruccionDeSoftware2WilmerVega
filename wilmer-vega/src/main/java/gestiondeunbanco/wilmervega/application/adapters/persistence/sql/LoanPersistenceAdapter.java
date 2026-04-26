package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanType;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.LoanRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.LoanEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.BankAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository repository;

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Loan save(Loan loan) {
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
            ClientEntity ce = new ClientEntity();
            ce.setId(model.getClientApplicant().getId());
            entity.setClientApplicant(ce);
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
        return model;
    }
}
