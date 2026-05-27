package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando los datos del cliente natural son inválidos o nulos.
 * Extiende BusinessException para mapeo centralizado a HTTP 400.
 */
public class InvalidNaturalClientException extends BusinessException {

    public InvalidNaturalClientException(String message) {
        super(message);
    }
}
