package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {
    private String accountType; // AHORROS, CORRIENTE, PERSONAL, EMPRESARIAL
    private String currency; // USD, COP, EUR
    private BigDecimal initialBalance;
}
