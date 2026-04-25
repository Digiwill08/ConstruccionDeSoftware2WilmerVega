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

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping({"/auth", "/api/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResult result = authUseCase.login(request.getUsername(), request.getPassword());
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Login exitoso");
            response.put("tokenType", result.getTokenType());
            response.put("token", result.getToken());
            response.put("username", result.getUsername());
            response.put("role", result.getRole());
            response.put("userId", result.getUserId());
            response.put("expiresInMs", result.getExpiresInMs());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResult result = authUseCase.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole()
            );
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Registro exitoso");
            response.put("tokenType", result.getTokenType());
            response.put("token", result.getToken());
            response.put("username", result.getUsername());
            response.put("role", result.getRole());
            response.put("userId", result.getUserId());
            response.put("expiresInMs", result.getExpiresInMs());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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