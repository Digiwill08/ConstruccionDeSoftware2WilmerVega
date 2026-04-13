package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.usecases.CompanySupervisorUseCase;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        try {
            return ResponseEntity.ok(companySupervisorUseCase.findTransferById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transfers/{id}/approve")
    public ResponseEntity<Transfer> approveTransfer(@PathVariable Long id,
                                                     @RequestParam Long supervisorUserId,
                                                     @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role) {
        try {
            return ResponseEntity.ok(companySupervisorUseCase.approveTransfer(id, supervisorUserId, role));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transfers/{id}/reject")
    public ResponseEntity<Transfer> rejectTransfer(@PathVariable Long id,
                                                    @RequestParam Long supervisorUserId,
                                                    @RequestParam(defaultValue = "COMPANY_SUPERVISOR") String role,
                                                    @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : null;
            return ResponseEntity.ok(companySupervisorUseCase.rejectTransfer(id, supervisorUserId, role, reason));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
