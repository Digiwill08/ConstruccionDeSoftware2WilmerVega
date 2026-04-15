package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferApprovalRequest {
    private Long transferId;
    private String actionType; // "APROBAR", "RECHAZAR"
}
