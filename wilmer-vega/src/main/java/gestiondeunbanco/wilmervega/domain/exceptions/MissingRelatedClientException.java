package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando un usuario con cierto rol requiere un cliente relacionado
 * pero este no fue proporcionado o no existe.
 */
public class MissingRelatedClientException extends BusinessException {

    public MissingRelatedClientException(String message) {
        super(message);
    }
}
