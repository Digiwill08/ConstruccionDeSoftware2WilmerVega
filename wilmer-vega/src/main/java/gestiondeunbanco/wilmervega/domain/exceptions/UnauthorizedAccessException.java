package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Exception thrown when a user attempts to access resources they don't have permission for.
 * Typically thrown when a client tries to access another client's data.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
