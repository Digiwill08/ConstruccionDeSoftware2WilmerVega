package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.application.usecases.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for INTERNAL_ANALYST (admin) role.
 * Endpoints: /api/admin/**
 * Exceptions handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUseCase adminUseCase;

    // ── Users ─────────────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminUseCase.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.findUserById(id));
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminUseCase.findUserByUsername(username));
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUseCase.saveUser(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminUseCase.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Audit Logs ────────────────────────────────────────────────────────────

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(adminUseCase.findAllAuditLogs());
    }

    @GetMapping("/audit-logs/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUseCase.findAuditLogsByUserId(userId));
    }
}
