package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando el número de documento tiene un formato inválido
 * (no es numérico, está vacío, etc.).
 */
public class InvalidDocumentNumberException extends BusinessException {

    public InvalidDocumentNumberException(String message) {
        super(message);
    }
}
