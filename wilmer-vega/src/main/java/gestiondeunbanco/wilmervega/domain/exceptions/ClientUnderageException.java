package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando el cliente natural no cumple con la edad mínima requerida.
 * La edad mínima para abrir productos bancarios es 18 años.
 */
public class ClientUnderageException extends BusinessException {

    public ClientUnderageException(String message) {
        super(message);
    }
}
