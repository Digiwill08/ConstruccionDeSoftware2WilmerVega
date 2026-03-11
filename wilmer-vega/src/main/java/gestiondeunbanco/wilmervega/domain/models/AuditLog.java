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
<<<<<<< Updated upstream
    private Date operationDateTime;
    private String userId;
    private String userRole;
    private String affectedProductId;
=======

    private Timestamp operationDateTime;

    private SystemUser user;

    private String userRole;

    private String affectedProductId;

    private String detailDataJson;


>>>>>>> Stashed changes
}

