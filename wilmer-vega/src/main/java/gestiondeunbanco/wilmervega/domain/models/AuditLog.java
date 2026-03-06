package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
public class AuditLog {
    private Long auditLogId;

    private OperationType operationType;
    private Timestamp operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;


}
