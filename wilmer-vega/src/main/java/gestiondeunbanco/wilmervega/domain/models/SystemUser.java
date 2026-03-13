package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SystemUser extends UserManager {

    private String relatedId;

    private NaturalClient naturalClient;

    private CompanyClient companyClient;

    private String identificationId;

    private SystemRole role;

    private List<Transfer> createdTransfers;

    private List<Transfer> approvedTransfers;

    private List<AuditLog> auditLogs;

}
