package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.usecases.AnalystUseCase;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Loan> approveLoan(@PathVariable Long id,
                                             @RequestParam Long analystUserId,
                                             @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        try {
            return ResponseEntity.ok(analystUseCase.approveLoan(id, analystUserId, role));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/loans/{id}/reject")
    public ResponseEntity<Loan> rejectLoan(@PathVariable Long id,
                                            @RequestParam Long analystUserId,
                                            @RequestParam(defaultValue = "INTERNAL_ANALYST") String role,
                                            @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : null;
            return ResponseEntity.ok(analystUseCase.rejectLoan(id, analystUserId, role, reason));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/loans/{id}/disburse")
    public ResponseEntity<Loan> disburseLoan(@PathVariable Long id,
                                              @RequestParam Long disbursementAccountId,
                                              @RequestParam Long analystUserId,
                                              @RequestParam(defaultValue = "INTERNAL_ANALYST") String role) {
        try {
            return ResponseEntity.ok(analystUseCase.disburseLoan(id, disbursementAccountId, analystUserId, role));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
