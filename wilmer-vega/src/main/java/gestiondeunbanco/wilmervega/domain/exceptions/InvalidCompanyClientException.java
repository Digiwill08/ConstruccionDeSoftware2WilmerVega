package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando los datos del cliente empresa son inválidos o nulos.
 * Extiende BusinessException para mapeo centralizado a HTTP 400.
 */
public class InvalidCompanyClientException extends BusinessException {

    public InvalidCompanyClientException(String message) {
        super(message);
    }
}
