package gestiondeunbanco.wilmervega.domain.exceptions;

import java.math.BigDecimal;

/**
 * Thrown when a transfer or operation is attempted but the source account
 * does not have sufficient funds. Maps to HTTP 422 Unprocessable Entity.
 */
public class InsufficientBalanceException extends RuntimeException {

    private final BigDecimal available;
    private final BigDecimal required;

    public InsufficientBalanceException(BigDecimal available, BigDecimal required) {
        super(String.format(
                "Saldo insuficiente. Disponible: %s, Requerido: %s", available, required));
        this.available = available;
        this.required = required;
    }

    public BigDecimal getAvailable() { return available; }
    public BigDecimal getRequired()  { return required; }
}
