package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {
    @NotBlank(message = "Account number is required")
    @Size(min = 4, max = 64, message = "Account number must be between 4 and 64 characters")
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    private String accountType; // SAVINGS, CHECKING, PERSONAL, BUSINESS

    @NotBlank(message = "Currency is required")
    private String currency; // USD, COP, EUR

    @NotNull(message = "Holder id is required")
    private Long holderId;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance must be greater or equal to zero")
    private BigDecimal initialBalance;
}
