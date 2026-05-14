package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "El monto de la transferencia es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0.01")
    private BigDecimal amount;

    @NotNull(message = "La cuenta de origen es obligatoria")
    private AccountRef sourceAccount;

    @NotNull(message = "La cuenta de destino es obligatoria")
    private AccountRef destinationAccount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountRef {
        @NotBlank(message = "El número de cuenta de origen es obligatorio")
        private String accountNumber;
    }
}
