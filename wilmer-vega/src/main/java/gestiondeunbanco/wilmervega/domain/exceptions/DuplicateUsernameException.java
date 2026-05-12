package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta crear un usuario con un nombre de usuario
 * que ya existe en el sistema.
 */
public class DuplicateUsernameException extends BusinessException {

    public DuplicateUsernameException(String message) {
        super(message);
    }
}
