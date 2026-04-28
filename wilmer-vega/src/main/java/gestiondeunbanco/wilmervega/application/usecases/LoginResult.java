package gestiondeunbanco.wilmervega.application.usecases;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResult {
    private String tokenType;
    private String token;
    private String username;
    private String role;
    private Long userId;
    private long expiresInMs;
}