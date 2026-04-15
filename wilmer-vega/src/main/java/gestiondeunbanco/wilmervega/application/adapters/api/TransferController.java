package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody TransferRequest request) {
        // TODO: Implementar creación de transferencia
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getMyTransfers() {
        // TODO: Implementar obtención de transferencias del cliente
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long transferId) {
        // TODO: Implementar obtención de transferencia por ID
        return ResponseEntity.ok(new TransferResponse());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransferResponse>> getTransfersByStatus(@PathVariable String status) {
        // TODO: Implementar obtención de transferencias por estado
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/approve")
    public ResponseEntity<TransferResponse> approveTransfer(@RequestBody TransferApprovalRequest request) {
        // TODO: Implementar aprobación de transferencia (solo COMPANY_SUPERVISOR)
        return ResponseEntity.ok(new TransferResponse());
    }

    @PostMapping("/reject")
    public ResponseEntity<TransferResponse> rejectTransfer(@RequestBody TransferApprovalRequest request) {
        // TODO: Implementar rechazo de transferencia (solo COMPANY_SUPERVISOR)
        return ResponseEntity.ok(new TransferResponse());
    }

    @GetMapping("/pending-approval")
    public ResponseEntity<List<TransferResponse>> getPendingApprovals() {
        // TODO: Implementar obtención de transferencias pendientes de aprobación
        return ResponseEntity.ok(List.of());
    }
}
