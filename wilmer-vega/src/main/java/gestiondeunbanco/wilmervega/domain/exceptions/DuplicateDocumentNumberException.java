package gestiondeunbanco.wilmervega.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta crear un cliente con un número de documento
 * que ya existe en otro cliente (natural o empresa).
 * Mantiene unicidad absoluta del documento entre todos los tipos de cliente.
 */
public class DuplicateDocumentNumberException extends BusinessException {

    public DuplicateDocumentNumberException(String message) {
        super(message);
    }
}
