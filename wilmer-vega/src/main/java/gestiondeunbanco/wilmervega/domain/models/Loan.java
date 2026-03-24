package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Loan {

    private Long loanId;

    private BankingProduct bankingProduct;

    private LoanType loanType;

    private NaturalClient naturalClientApplicant;

    private CompanyClient companyClientApplicant;

    private BigDecimal requestedAmount;

    private BigDecimal approvedAmount;

    private BigDecimal interestRate;

    private Integer termInMonths;

    private LoanStatus loanStatus;

    private Date approvalDate;

    private Date disbursementDate;

    private BankAccount disbursementAccount;
}
