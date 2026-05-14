package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.ErrorResponse;
import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.BusinessException;
import gestiondeunbanco.wilmervega.domain.exceptions.InsufficientBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidCredentialsException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.exceptions.OwnershipViolationException;
import gestiondeunbanco.wilmervega.domain.exceptions.UnauthorizedAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler — centraliza todas las respuestas de error HTTP.
 * Cubre: excepciones de dominio, seguridad, fallos de MongoDB y errores genéricos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 — Recurso no encontrado */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        String message = String.format(
            "%s. Acción recomendada: verifica el ID del recurso y vuelve a intentar. Si el problema persiste, contacta con soporte.",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, message));
    }

    /** 409 — Violación de regla de negocio */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        String message = String.format(
            "Operación rechazada: %s. Por favor revisa las condiciones de la operación e intenta nuevamente.",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, message));
    }

    /** 401 — Credenciales inválidas */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401, "No autenticado: verifica tu usuario y contraseña. Si olvidaste tu contraseña, usa la opción de recuperación."));
    }

    /** 400 — Errores de validación Bean Validation (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        String message = String.format(
            "Validación fallida en %d campo(s). Revisa los datos ingresados y asegúrate de cumplir con los requisitos.",
            errors.size()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, message, errors));
    }

    /** 400 — Argumento inválido / estado ilegal */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        // Para mensajes específicos de negocio (ej: "Username already exists"), mantén el mensaje tal cual
        if (ex.getMessage() != null && (ex.getMessage().contains("already exists") || ex.getMessage().contains("bloqueada") || ex.getMessage().contains("Transferencia"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, ex.getMessage()));
        }
        // Para otros casos, añade contexto
        String message = String.format(
            "Solicitud inválida: %s. Por favor verifica los parámetros y el estado de la operación.",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, message));
    }

    /** 403 — Violacion de propiedad: el recurso no pertenece al usuario autenticado */
    @ExceptionHandler(OwnershipViolationException.class)
    public ResponseEntity<ErrorResponse> handleOwnershipViolation(OwnershipViolationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Acceso prohibido: " + ex.getMessage() + " Solo el propietario del recurso puede realizar esta operación."));
    }

    /** 403 — Acceso no autorizado a recurso/operacion */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Acceso prohibido: " + ex.getMessage() + " Solicita permisos adicionales si es necesario."));
    }

    /** 422 — Saldo insuficiente para completar la operacion */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(422, ex.getMessage() + " Opciones: deposita dinero a tu cuenta o intenta una transferencia de menor monto."));
    }

    /** 409 — Cuenta bloqueada o cancelada */
    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountBlocked(AccountBlockedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, ex.getMessage() + " Por favor contacta a nuestro equipo de soporte para más información."));
    }

    /** 403 — Acceso denegado */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Acceso denegado: no tienes permisos para acceder a este recurso. Si crees que es un error, contacta a administración."));
    }

    /** 401 — No autenticado */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401, "No autenticado. Inicia sesión nuevamente para continuar."));
    }

    /** 409 — Conflicto de versión (OptimisticLocking) */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "La operación fue modificada por otro usuario. Por favor vuelve a intentar. Si el problema persiste, contacta a soporte."));
    }

    /** 400 — JSON malformado o parsing error */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "JSON inválido. Por favor verifica el formato de tu solicitud: asegúrate de usar comillas correctas, paréntesis balanceados y tipos de dato válidos."));
    }

    /** 503 — MongoDB / DataSource no disponible */
    @ExceptionHandler({DataAccessResourceFailureException.class, MongoTimeoutException.class,
                       MongoException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> handleMongoFailures(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(503, "Servicio de auditoria temporalmente no disponible. Por favor intenta en unos momentos. Si el problema persiste, contacta a soporte."));
    }

    /** 500 — Error inesperado del servidor */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Ocurrió un error interno en el servidor. Nuestro equipo ha sido notificado. Por favor intenta la operación más tarde."));
    }
}
