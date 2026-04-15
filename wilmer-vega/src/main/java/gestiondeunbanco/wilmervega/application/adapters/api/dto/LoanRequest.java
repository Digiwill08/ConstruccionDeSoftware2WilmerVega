package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {
    private String loanType; // CONSUMO, VEHICULO, HIPOTECARIO, EMPRESARIAL
    private BigDecimal requestedAmount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private Long disbursementAccountId;
}
