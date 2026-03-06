package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
public class Loan {
    private Long loanId;

    private LoanType loanType;
    private String applicantClientId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private LoanStatus loanStatus;
    private Date approvalDate;
    private Date disbursementDate;
    private String disbursementAccountNumber;


}
