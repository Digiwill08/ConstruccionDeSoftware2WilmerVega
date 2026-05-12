package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando los datos de la cuenta bancaria son inválidos o nulos.
 * Extiende BusinessException para mapeo centralizado a HTTP 400.
 */
public class InvalidBankAccountException extends BusinessException {

    public InvalidBankAccountException(String message) {
        super(message);
    }
}
