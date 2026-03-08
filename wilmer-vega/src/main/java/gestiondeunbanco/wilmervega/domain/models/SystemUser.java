package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class SystemUser {

    private Long id;

    private String relatedId;

    private String fullName;

    private String identificationId;

    private String email;

    private String phone;

    private Date birthDate;

    private String address;

    private SystemRole role;

    private UserStatus userStatus;

}
