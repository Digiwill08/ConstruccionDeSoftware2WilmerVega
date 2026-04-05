package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;





import java.time.LocalDateTime;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor

public class AuditLog {
    
    
    private Long auditLogId;

    private OperationType operationType;

    private LocalDateTime operationDateTime;

    private Long userId;

    
    private SystemUser user;

    private String userRole;

    private String affectedProductId;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> details;
}

