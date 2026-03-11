package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class SystemUser {

<<<<<<< Updated upstream
    private Long id;

    private String relatedId;

    private String fullName;

    private String identificationId;
=======
    private NaturalClient naturalClient;

    private CompanyClient companyClient;

    private String fullName;

    private String identificationNumber;
>>>>>>> Stashed changes

    private String email;

    private String phone;

    private Date birthDate;

    private String address;

<<<<<<< Updated upstream
    private SystemRole role;

    private UserStatus userStatus;
=======
    private SystemRole systemRole;

    private UserStatus userStatus;

    private List<Transfer> createdTransfers;

    private List<Transfer> approvedTransfers;

    private List<AuditLog> auditLogs;


>>>>>>> Stashed changes
}
