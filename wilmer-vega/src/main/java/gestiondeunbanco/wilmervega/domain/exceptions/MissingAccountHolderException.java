package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta crear una cuenta bancaria sin un titular
 * o cuando el titular especificado no existe en el sistema.
 */
public class MissingAccountHolderException extends BusinessException {

    public MissingAccountHolderException(String message) {
        super(message);
    }
}
