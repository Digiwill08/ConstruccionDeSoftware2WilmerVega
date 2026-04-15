package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApprovalRequest {
    private Long loanId;
    private String actionType; // "APROBAR", "RECHAZAR", "DESEMBOLSAR"
    private BigDecimal approvedAmount;
}
