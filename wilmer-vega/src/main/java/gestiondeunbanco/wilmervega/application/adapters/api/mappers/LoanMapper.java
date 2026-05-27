package gestiondeunbanco.wilmervega.application.adapters.api.mappers;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.LoanRequest;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public Loan toModel(LoanRequest request) {
        Loan model = new Loan();
        model.setLoanType(request.getLoanType());
        model.setRequestedAmount(request.getRequestedAmount());
        model.setApprovedAmount(request.getApprovedAmount());
        model.setInterestRate(request.getInterestRate());
        model.setTermInMonths(request.getTermInMonths());
        model.setLoanStatus(request.getLoanStatus());

        if (request.getClientApplicant() != null && request.getClientApplicant().getId() != null) {
            NaturalClient applicant = new NaturalClient();
            applicant.setId(request.getClientApplicant().getId());
            model.setClientApplicant(applicant);
        }

        return model;
    }
}