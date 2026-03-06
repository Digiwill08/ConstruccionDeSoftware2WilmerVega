package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
public class SystemUser {
    private Long userId;

    private Long relatedEntityId;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private Date birthDate;
    private String address;
    private SystemRole systemRole;
    private UserStatus userStatus;


}
