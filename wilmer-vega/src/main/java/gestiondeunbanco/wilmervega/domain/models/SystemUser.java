package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SystemUser {

    private Long id;

    private String relatedId;

    private NaturalClient naturalClient;

    private CompanyClient companyClient;

    private String fullName;

    private String identificationId;

    private String email;

    private String phone;

    private Date birthDate;

    private String address;

    private SystemRole role;

    private UserStatus userStatus;

    private List<Transfer> createdTransfers;

    private List<Transfer> approvedTransfers;

    private List<AuditLog> auditLogs;

}
