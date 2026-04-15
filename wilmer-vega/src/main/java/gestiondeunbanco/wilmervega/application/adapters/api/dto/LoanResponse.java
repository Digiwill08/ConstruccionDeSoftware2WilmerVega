package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {
    private Long id;
    private String loanType;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private String status;
    private String approvalDate;
    private String disbursementDate;
}
