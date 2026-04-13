package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Loan {

    private Long loanId;

    private BankingProduct bankingProduct;

    private LoanType loanType;

    private Client clientApplicant;

    private BigDecimal requestedAmount;

    private BigDecimal approvedAmount;

    private BigDecimal interestRate;

    private Integer termInMonths;

    private LoanStatus loanStatus;

    private LocalDate approvalDate;

    private LocalDate disbursementDate;

    private BankAccount disbursementAccount;

    private Long approvedByUserId;
}
