package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.sql.Date;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
public class AuditLog {
    private Long auditLogId;

    private OperationType operationType;
    private Date operationDateTime;
    private String userId;
    private String userRole;
    private String affectedProductId;


}

