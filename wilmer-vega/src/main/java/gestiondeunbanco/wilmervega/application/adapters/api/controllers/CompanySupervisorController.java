package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.CompanySupervisorUseCase;
import gestiondeunbanco.wilmervega.config.security.ClientAccessContext;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMPANY_SUPERVISOR')")
public class CompanySupervisorController {

    private final CompanySupervisorUseCase companySupervisorUseCase;

    @GetMapping("/transfers/pending")
    public ResponseEntity<List<Transfer>> getPendingTransfers() {
        return ResponseEntity.ok(companySupervisorUseCase.findPendingTransfers());
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(companySupervisorUseCase.findTransferById(id));
    }

    @PostMapping("/transfers/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveTransfer(@PathVariable Long id) {
        // Obtain supervisorUserId and role from authenticated JWT context (NOT from query params)
        // This prevents role injection attacks where a client could manipulate their role
        Long supervisorUserId = ClientAccessContext.getCurrentUserId();
        String role = ClientAccessContext.getCurrentRole();
        
        if (supervisorUserId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Usuario no autenticado"
            ));
        }
        
        Transfer saved = companySupervisorUseCase.approveTransfer(id, supervisorUserId, role);
        return ResponseEntity.ok(Map.of(
                "message", "Transferencia aprobada correctamente",
                "id", saved.getTransferId(),
                "status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : ""
        ));
    }

    @PostMapping("/transfers/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectTransfer(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        // Obtain supervisorUserId and role from authenticated JWT context (NOT from query params)
        // This prevents role injection attacks where a client could manipulate their role
        Long supervisorUserId = ClientAccessContext.getCurrentUserId();
        String role = ClientAccessContext.getCurrentRole();
        
        if (supervisorUserId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Usuario no autenticado"
            ));
        }
        
        String reason = body != null ? body.get("reason") : null;
        Transfer saved = companySupervisorUseCase.rejectTransfer(id, supervisorUserId, role, reason);
        return ResponseEntity.ok(Map.of(
                "message", "Transferencia rechazada correctamente",
                "id", saved.getTransferId(),
                "status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : ""
        ));
    }
}
