package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando los datos del usuario son inválidos o nulos.
 * Extiende BusinessException para mapeo centralizado a HTTP 400.
 */
public class InvalidUserException extends BusinessException {

    public InvalidUserException(String message) {
        super(message);
    }
}
