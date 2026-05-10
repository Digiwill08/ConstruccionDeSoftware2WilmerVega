package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.CompanySupervisorUseCase;
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
    public ResponseEntity<Map<String, Object>> approveTransfer(
            @PathVariable Long id,
            @RequestParam Long supervisorUserId,
            @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role) {
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
            @RequestParam Long supervisorUserId,
            @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        Transfer saved = companySupervisorUseCase.rejectTransfer(id, supervisorUserId, role, reason);
        return ResponseEntity.ok(Map.of(
                "message", "Transferencia rechazada correctamente",
                "id", saved.getTransferId(),
                "status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : ""
        ));
    }
}
