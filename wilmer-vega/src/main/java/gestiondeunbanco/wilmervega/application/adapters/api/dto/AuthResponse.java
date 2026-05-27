package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import gestiondeunbanco.wilmervega.application.usecases.LoginResult;

public record AuthResponse(
        String message,
        String tokenType,
        String token,
        String username,
        String role,
        Long userId,
        long expiresInMs
) {
    public static AuthResponse loginSuccess(LoginResult result) {
        return new AuthResponse(
                "Login exitoso",
                result.getTokenType(),
                result.getToken(),
                result.getUsername(),
                result.getRole(),
                result.getUserId(),
                result.getExpiresInMs()
        );
    }

    public static AuthResponse registerSuccess(LoginResult result) {
        return new AuthResponse(
                "Registro exitoso",
                result.getTokenType(),
                result.getToken(),
                result.getUsername(),
                result.getRole(),
                result.getUserId(),
                result.getExpiresInMs()
        );
    }
}