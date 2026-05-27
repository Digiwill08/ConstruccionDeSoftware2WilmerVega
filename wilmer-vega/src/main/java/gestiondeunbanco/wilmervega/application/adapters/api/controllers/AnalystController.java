package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.AnalystUseCase;
import gestiondeunbanco.wilmervega.config.security.ClientAccessContext;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller para el rol INTERNAL_ANALYST.
 * Endpoints: /api/analyst/**
 *
 * Segregacion de Funciones:
 *  - Solo INTERNAL_ANALYST puede aprobar/rechazar/desembolsar prestamos
 *  - Solo INTERNAL_ANALYST puede consultar bitacoras de auditoria
 */
@RestController
@RequestMapping("/api/analyst")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INTERNAL_ANALYST')")
public class AnalystController {

    private final AnalystUseCase analystUseCase;

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(analystUseCase.findAllLoans());
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(analystUseCase.findLoanById(id));
    }

    @PostMapping("/loans/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveLoan(
            @PathVariable Long id,
            @RequestParam Long analystUserId,
            @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        Loan saved = analystUseCase.approveLoan(id, analystUserId, role);
        return ResponseEntity.ok(Map.of(
                "message", "Prestamo aprobado correctamente",
                "id", saved.getLoanId(),
                "status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : ""
        ));
    }

    @PostMapping("/loans/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectLoan(
            @PathVariable Long id,
            @RequestParam Long analystUserId,
            @RequestParam(defaultValue = "INTERNAL_ANALYST") String role,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        Loan saved = analystUseCase.rejectLoan(id, analystUserId, role, reason);
        return ResponseEntity.ok(Map.of(
                "message", "Prestamo rechazado correctamente",
                "id", saved.getLoanId(),
                "status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : ""
        ));
    }

    @PostMapping("/loans/{id}/disburse")
    public ResponseEntity<Map<String, Object>> disburseLoan(
            @PathVariable Long id,
            @RequestParam Long disbursementAccountId,
            @RequestParam Long analystUserId,
            @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        Loan saved = analystUseCase.disburseLoan(id, disbursementAccountId, analystUserId, role);
        return ResponseEntity.ok(Map.of(
                "message", "Prestamo desembolsado correctamente",
                "id", saved.getLoanId(),
                "status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : ""
        ));
    }

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
