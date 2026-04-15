package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {
    private String accountNumber;
    private String accountType; // SAVINGS, CHECKING, PERSONAL, BUSINESS
    private String currency; // USD, COP, EUR
    private Long holderId;
    private BigDecimal initialBalance;
}
