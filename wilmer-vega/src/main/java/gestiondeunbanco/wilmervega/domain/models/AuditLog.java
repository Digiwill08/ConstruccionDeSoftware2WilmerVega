package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    private String auditLogId;

    private OperationType operationType;

    private LocalDateTime operationDateTime;

    private Long userId;

    private String userRole;

    private String affectedProductId;

    private Map<String, Object> details;
}
