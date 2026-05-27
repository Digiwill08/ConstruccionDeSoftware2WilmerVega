package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Thrown when a client attempts to access or modify a resource
 * that does not belong to them (ownership violation).
 * Maps to HTTP 403 Forbidden in the GlobalExceptionHandler.
 */
public class OwnershipViolationException extends RuntimeException {

    public OwnershipViolationException(String message) {
        super(message);
    }

    public OwnershipViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
