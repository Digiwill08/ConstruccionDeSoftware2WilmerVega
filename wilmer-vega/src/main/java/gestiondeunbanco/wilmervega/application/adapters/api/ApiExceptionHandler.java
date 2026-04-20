package gestiondeunbanco.wilmervega.application.adapters.api;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({DataAccessResourceFailureException.class, MongoTimeoutException.class, MongoException.class, DataAccessException.class})
    public ResponseEntity<Map<String, String>> handleMongoFailures(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Audit log service temporarily unavailable",
                        "message", ex.getMessage() == null ? "MongoDB no disponible" : ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Unexpected error",
                        "message", ex.getMessage() == null ? "Sin detalle" : ex.getMessage()
                ));
    }
}
