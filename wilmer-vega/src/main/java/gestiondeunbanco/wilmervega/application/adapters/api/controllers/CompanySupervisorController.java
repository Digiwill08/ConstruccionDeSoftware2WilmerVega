package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.CompanySupervisorUseCase;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller for the COMPANY_SUPERVISOR role.
 * Endpoints: /api/supervisor/**
 */
@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
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
    public ResponseEntity<Map<String, Object>> approveTransfer(@PathVariable Long id,
                                                     @RequestParam Long supervisorUserId,
                                                     @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role) {
        try {
            Transfer saved = companySupervisorUseCase.approveTransfer(id, supervisorUserId, role);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Transferencia aprobada correctamente");
            response.put("id", saved.getTransferId());
            response.put("status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/transfers/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectTransfer(@PathVariable Long id,
                                                    @RequestParam Long supervisorUserId,
                                                    @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role,
                                                    @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : null;
            Transfer saved = companySupervisorUseCase.rejectTransfer(id, supervisorUserId, role, reason);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Transferencia rechazada correctamente");
            response.put("id", saved.getTransferId());
            response.put("status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
