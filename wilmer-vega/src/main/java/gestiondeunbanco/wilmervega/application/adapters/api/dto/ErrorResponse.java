package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta de error estructurada y tipada para todos los endpoints.
 * Reemplaza el uso de Map<String, Object> en el GlobalExceptionHandler.
 */
@Getter
public class ErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final List<String> fields;

    /** Constructor para errores simples (sin lista de campos). */
    public ErrorResponse(int status, String error) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.fields = null;
    }

    /** Constructor para errores de validación (con lista de campos fallidos). */
    public ErrorResponse(int status, String error, List<String> fields) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.fields = fields;
    }

    /** Alias para compatibilidad con clientes/pruebas que esperan la propiedad "message". */
    public String getMessage() {
        return error;
    }
}
