package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.usecases.AnalystUseCase;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller for the INTERNAL_ANALYST role.
 * Endpoints: /api/analyst/**
 */
@RestController
@RequestMapping("/api/analyst")
@RequiredArgsConstructor
public class AnalystController {

    private final AnalystUseCase analystUseCase;

    // --- Loans ---

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(analystUseCase.findAllLoans());
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(analystUseCase.findLoanById(id));
    }

    @PostMapping("/loans/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveLoan(@PathVariable Long id,
                                             @RequestParam Long analystUserId,
                                             @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        try {
            Loan saved = analystUseCase.approveLoan(id, analystUserId, role);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Prestamo aprobado correctamente");
            response.put("id", saved.getLoanId());
            response.put("status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/loans/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectLoan(@PathVariable Long id,
                                            @RequestParam Long analystUserId,
                                            @RequestParam(defaultValue = "INTERNAL_ANALYST") String role,
                                            @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : null;
            Loan saved = analystUseCase.rejectLoan(id, analystUserId, role, reason);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Prestamo rechazado correctamente");
            response.put("id", saved.getLoanId());
            response.put("status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/loans/{id}/disburse")
    public ResponseEntity<Map<String, Object>> disburseLoan(@PathVariable Long id,
                                              @RequestParam Long disbursementAccountId,
                                              @RequestParam Long analystUserId,
                                              @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        try {
            Loan saved = analystUseCase.disburseLoan(id, disbursementAccountId, analystUserId, role);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Prestamo desembolsado correctamente");
            response.put("id", saved.getLoanId());
            response.put("status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // --- Audit Log ---

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(analystUseCase.findAllAuditLogs());
    }

    @GetMapping("/audit-logs/product/{productId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(analystUseCase.findAuditLogsByProduct(productId));
    }

    @GetMapping("/audit-logs/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(analystUseCase.findAuditLogsByUser(userId));
    }
}
