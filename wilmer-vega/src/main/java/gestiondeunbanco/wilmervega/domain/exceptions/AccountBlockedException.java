package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Thrown when an operation is attempted on a BLOCKED or CANCELLED bank account.
 * Maps to HTTP 409 Conflict in the GlobalExceptionHandler.
 */
public class AccountBlockedException extends RuntimeException {

    public AccountBlockedException(String accountNumber, String status) {
        super(String.format(
                "La cuenta '%s' no está activa. Estado actual: %s", accountNumber, status));
    }
}
