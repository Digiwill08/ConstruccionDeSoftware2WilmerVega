package gestiondeunbanco.wilmervega.application.adapters.api;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.exception.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Global exception handler — centralizes all HTTP error responses.
 * Covers: domain exceptions, security, MongoDB failures, and generic errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 — Resource not found */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(404, ex.getMessage()));
    }

    /** 409 — Business rule violation */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(409, ex.getMessage()));
    }

    /** 400 — Invalid argument / bad state */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(400, ex.getMessage()));
    }

    /** 403 — Access denied */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorBody(403, "Acceso denegado: no tiene permisos para este recurso"));
    }

    /** 401 — Not authenticated */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorBody(401, "No autenticado: " + ex.getMessage()));
    }

    /** 503 — MongoDB / DataSource unavailable */
    @ExceptionHandler({DataAccessResourceFailureException.class, MongoTimeoutException.class,
                       MongoException.class, DataAccessException.class})
    public ResponseEntity<Map<String, Object>> handleMongoFailures(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorBody(503, "Servicio de auditoría temporalmente no disponible"));
    }

    /** 500 — Unexpected server error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody(500, "Error interno del servidor: " + ex.getMessage()));
    }

    private Map<String, Object> errorBody(int status, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status,
                "error", message != null ? message : "Error desconocido"
        );
    }
}
