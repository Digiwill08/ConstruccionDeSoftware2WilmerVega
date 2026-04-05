package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.application.usecases.AuditLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogUseCase auditLogUseCase;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(auditLogUseCase.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogUseCase.findByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuditLog>> getByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(auditLogUseCase.findByAffectedProductId(productId));
    }

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody AuditLog auditLog) {
        return ResponseEntity.ok(auditLogUseCase.save(auditLog));
    }
}
