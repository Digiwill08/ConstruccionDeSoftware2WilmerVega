package gestiondeunbanco.wilmervega.application.adapters.persistence.mongo;

import gestiondeunbanco.wilmervega.domain.models.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * MongoDB document entity for the AuditLog (Bitácora).
 * Stored in the "audit_log" collection.
 */
@Document(collection = "audit_log")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogDocument {

    @Id
    private String id;

    private OperationType operationType;

    private LocalDateTime operationDateTime;

    private Long userId;

    private String userRole;

    private String affectedProductId;

    private Map<String, Object> details;
}
