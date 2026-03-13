package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SystemUser extends Persona {

    private String relatedId;

    private NaturalClient naturalClient;

    private CompanyClient companyClient;

    private String fullName;

    private String identificationId;

    private Date birthDate;

    private SystemRole role;

    private UserStatus userStatus;

    private List<Transfer> createdTransfers;

    private List<Transfer> approvedTransfers;

    private List<AuditLog> auditLogs;

}
