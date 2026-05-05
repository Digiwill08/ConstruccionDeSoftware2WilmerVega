package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {

    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String accountNumber;

    @NotBlank(message = "El tipo de cuenta es obligatorio (SAVINGS, CHECKING, PERSONAL, BUSINESS)")
    private String accountType;

    @NotBlank(message = "La moneda es obligatoria (USD, COP, EUR)")
    private String currency;

    @NotNull(message = "El titular de la cuenta es obligatorio")
    private Long holderId;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a cero")
    private BigDecimal initialBalance;
}
