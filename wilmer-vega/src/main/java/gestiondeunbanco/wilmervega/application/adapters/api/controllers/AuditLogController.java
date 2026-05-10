package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.AdminUseCase;
import gestiondeunbanco.wilmervega.application.usecases.AnalystUseCase;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INTERNAL_ANALYST')")
public class AuditLogController {

    private final AnalystUseCase analystUseCase;
    private final AdminUseCase adminUseCase;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(analystUseCase.findAllAuditLogs());
    }

    @GetMapping("/my-operations")
    public ResponseEntity<List<AuditLog>> getMyOperations(@RequestParam String username) {
        Long userId = adminUseCase.findUserByUsername(username).getUserId();
        return ResponseEntity.ok(analystUseCase.findAuditLogsByUser(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuditLog>> getLogsByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(analystUseCase.findAuditLogsByProduct(productId));
    }

    @GetMapping("/operation-type/{operationType}")
    public ResponseEntity<List<AuditLog>> getLogsByOperationType(@PathVariable String operationType) {
        String normalized = operationType.toUpperCase(Locale.ROOT);
        List<AuditLog> filtered = analystUseCase.findAllAuditLogs().stream()
                .filter(log -> log.getOperationType() != null
                        && log.getOperationType().name().equals(normalized))
                .toList();
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(analystUseCase.findAuditLogsByUser(userId));
    }

    /**
     * Filtra bitacoras por rango de fechas (formato: yyyy-MM-dd).
     * Si el formato es invalido, DateTimeParseException → GlobalExceptionHandler → 400.
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLog>> getLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end   = LocalDate.parse(endDate).atTime(23, 59, 59);
        List<AuditLog> filtered = analystUseCase.findAllAuditLogs().stream()
                .filter(log -> log.getOperationDateTime() != null
                        && !log.getOperationDateTime().isBefore(start)
                        && !log.getOperationDateTime().isAfter(end))
                .toList();
        return ResponseEntity.ok(filtered);
    }
}
