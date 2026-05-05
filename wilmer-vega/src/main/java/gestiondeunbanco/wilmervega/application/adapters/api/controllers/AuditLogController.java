package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.AdminUseCase;
import gestiondeunbanco.wilmervega.application.usecases.AnalystUseCase;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AnalystUseCase analystUseCase;
    private final AdminUseCase adminUseCase;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return executeWithTimeout(() -> analystUseCase.findAllAuditLogs());
    }

    @GetMapping("/my-operations")
    public ResponseEntity<List<AuditLog>> getMyOperations(@RequestParam String username) {
        Long userId = adminUseCase.findUserByUsername(username).getUserId();
        return executeWithTimeout(() -> analystUseCase.findAuditLogsByUser(userId));
    }



    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuditLog>> getLogsByProduct(@PathVariable String productId) {
        return executeWithTimeout(() -> analystUseCase.findAuditLogsByProduct(productId));
    }

    @GetMapping("/operation-type/{operationType}")
    public ResponseEntity<List<AuditLog>> getLogsByOperationType(@PathVariable String operationType) {
        List<AuditLog> filtered = analystUseCase.findAllAuditLogs().stream()
                .filter(log -> log.getOperationType() != null
                        && log.getOperationType().name().equals(operationType.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable Long userId) {
        return executeWithTimeout(() -> analystUseCase.findAuditLogsByUser(userId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLog>> getLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

            List<AuditLog> filtered = analystUseCase.findAllAuditLogs().stream()
                    .filter(log -> log.getOperationDateTime() != null
                            && !log.getOperationDateTime().isBefore(start)
                            && !log.getOperationDateTime().isAfter(end))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filtered);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<List<AuditLog>> executeWithTimeout(java.util.function.Supplier<List<AuditLog>> action) {
        try {
            List<AuditLog> result = CompletableFuture.supplyAsync(action)
                    .get(2, TimeUnit.SECONDS);
            return ResponseEntity.ok(result);
        } catch (TimeoutException ex) {
            return ResponseEntity.status(503).build();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(503).build();
        } catch (ExecutionException ex) {
            return ResponseEntity.status(503).build();
        }
    }
}
