package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta crear una cuenta bancaria con un número
 * que ya existe en el sistema.
 */
public class DuplicateAccountNumberException extends BusinessException {

    public DuplicateAccountNumberException(String message) {
        super(message);
    }
}
