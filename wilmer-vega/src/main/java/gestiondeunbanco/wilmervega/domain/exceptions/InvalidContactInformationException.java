package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando la información de contacto (email, teléfono, dirección)
 * es inválida o incompleta.
 */
public class InvalidContactInformationException extends BusinessException {

    public InvalidContactInformationException(String message) {
        super(message);
    }
}
