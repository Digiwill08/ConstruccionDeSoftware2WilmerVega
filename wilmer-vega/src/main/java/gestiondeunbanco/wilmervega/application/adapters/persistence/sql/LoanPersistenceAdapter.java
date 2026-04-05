package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanType;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.LoanRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.LoanEntity;
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
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = toEntity(loan);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
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
        if (model.getNaturalClientApplicant() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity nc = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity();
            nc.setId(model.getNaturalClientApplicant().getId());
            entity.setNaturalClientApplicant(nc);
        }
        if (model.getCompanyClientApplicant() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity cc = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity();
            cc.setId(model.getCompanyClientApplicant().getId());
            entity.setCompanyClientApplicant(cc);
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
        if (entity.getNaturalClientApplicant() != null) {
            gestiondeunbanco.wilmervega.domain.models.NaturalClient c = new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
            c.setId(entity.getNaturalClientApplicant().getId());
            model.setNaturalClientApplicant(c);
        }
        if (entity.getCompanyClientApplicant() != null) {
            gestiondeunbanco.wilmervega.domain.models.CompanyClient c = new gestiondeunbanco.wilmervega.domain.models.CompanyClient();
            c.setId(entity.getCompanyClientApplicant().getId());
            model.setCompanyClientApplicant(c);
        }
        return model;
    }
}
