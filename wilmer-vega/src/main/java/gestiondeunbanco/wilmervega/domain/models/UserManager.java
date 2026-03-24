package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserManager extends Person {

    private String fullName;

    private Date birthDate;

    private UserStatus userStatus;
}
