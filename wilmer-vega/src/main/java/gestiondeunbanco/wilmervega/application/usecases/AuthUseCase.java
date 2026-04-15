package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.config.security.JwtService;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthUseCase {

    private static final String INVALID_CREDENTIALS = "Invalid username or password";

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResult login(String username, String rawPassword) {
        if (username == null || username.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        User user = userPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CREDENTIALS));

        if (!passwordMatchesAndMigrateIfNeeded(user, rawPassword)) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        if (user.getSystemRole() == null) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getSystemRole().name());
        claims.put("userId", user.getUserId());

        String token = jwtService.generateToken(user.getUsername(), claims);

        return new LoginResult(
                "Bearer",
                token,
                user.getUsername(),
                user.getSystemRole().name(),
                user.getUserId(),
                jwtService.getExpirationMs()
        );
    }

    private boolean passwordMatchesAndMigrateIfNeeded(User user, String rawPassword) {
        String storedPassword = user.getPassword();
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (looksEncoded(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        if (storedPassword.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userPort.save(user);
            return true;
        }

        return false;
    }

    private boolean looksEncoded(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}