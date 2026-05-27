package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.domain.models.LoanType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @NotNull(message = "El tipo de prestamo es obligatorio")
    private LoanType loanType;

    @Valid
    @NotNull(message = "El cliente solicitante es obligatorio")
    private ClientRef clientApplicant;

    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto solicitado debe ser mayor a 0")
    private BigDecimal requestedAmount;

    @NotNull(message = "El monto aprobado es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto aprobado debe ser mayor o igual a 0")
    private BigDecimal approvedAmount;

    @NotNull(message = "La tasa de interes es obligatoria")
    @DecimalMin(value = "0.0", message = "La tasa de interes debe ser mayor o igual a 0")
    private BigDecimal interestRate;

    @NotNull(message = "El plazo en meses es obligatorio")
    @DecimalMin(value = "1", message = "El plazo debe ser al menos de 1 mes")
    private Integer termInMonths;

    @NotNull(message = "El estado del prestamo es obligatorio")
    private LoanStatus loanStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientRef {
        @NotNull(message = "El ID del cliente solicitante es obligatorio")
        private Long id;
    }
}