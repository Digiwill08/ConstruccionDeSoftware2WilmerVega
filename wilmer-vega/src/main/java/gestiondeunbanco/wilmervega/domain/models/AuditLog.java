package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class AuditLog {
    private Long auditLogId;

    private OperationType operationType;

    private Date operationDateTime;

    private SystemUser user;

    private String userRole;

    private String affectedProductId;

    private String detailDataJson;
}

