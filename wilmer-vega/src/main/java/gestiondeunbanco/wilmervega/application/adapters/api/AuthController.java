package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.usecases.AuthUseCase;
import gestiondeunbanco.wilmervega.application.usecases.LoginResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"/auth", "/api/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResult result = authUseCase.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResult result = authUseCase.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile(Authentication authentication) {
        Authentication currentAuthentication = authentication != null
                ? authentication
                : SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication == null || currentAuthentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        return ResponseEntity.ok(authUseCase.getProfile(currentAuthentication.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}