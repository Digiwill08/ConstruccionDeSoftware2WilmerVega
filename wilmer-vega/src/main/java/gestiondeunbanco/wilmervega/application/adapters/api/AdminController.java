package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.application.usecases.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUseCase adminUseCase;

    // --- Users ---
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminUseCase.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminUseCase.findUserById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(adminUseCase.findUserByUsername(username));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User saved = adminUseCase.saveUser(user);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminUseCase.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Audit Logs ---
    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(adminUseCase.findAllAuditLogs());
    }

    @GetMapping("/audit-logs/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUseCase.findAuditLogsByUserId(userId));
    }
}
