package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.config.security.JwtService;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidCredentialsException;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
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

    public LoginResult register(String username, String rawPassword, String requestedRole) {
        if (username == null || username.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        if (userPort.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        SystemRole role = resolveAllowedPublicRole(requestedRole);

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setSystemRole(role);
        userPort.save(user);

        return login(username, rawPassword);
    }

    public LoginResult login(String username, String rawPassword) {
        if (username == null || username.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        User user = userPort.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        if (!passwordMatchesAndMigrateIfNeeded(user, rawPassword)) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        if (user.getSystemRole() == null) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
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

    public Map<String, Object> getProfile(String username) {
        User user = userPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CREDENTIALS));

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        profile.put("role", user.getSystemRole() != null ? user.getSystemRole().name() : null);
        profile.put("userId", user.getUserId());
        profile.put("email", user.getRelatedClient() != null ? user.getRelatedClient().getEmail() : null);
        profile.put("documentNumber", user.getRelatedClient() != null ? user.getRelatedClient().getDocumentNumber() : null);
        return profile;
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

    private SystemRole resolveAllowedPublicRole(String requestedRole) {
        if (requestedRole == null || requestedRole.isBlank()) {
            return SystemRole.NATURAL_CLIENT;
        }

        SystemRole role;
        try {
            role = SystemRole.valueOf(requestedRole.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role");
        }

        // Public registration is limited to client-side roles.
        if (role != SystemRole.NATURAL_CLIENT &&
            role != SystemRole.COMPANY_CLIENT &&
            role != SystemRole.COMPANY_EMPLOYEE) {
            throw new IllegalArgumentException("Role is not allowed for public registration");
        }

        return role;
    }
}