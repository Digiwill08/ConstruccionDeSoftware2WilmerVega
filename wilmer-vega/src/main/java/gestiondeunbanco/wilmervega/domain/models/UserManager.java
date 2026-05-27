package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserManager extends Person {

    private String fullName;

    private LocalDate birthDate;

    private UserStatus userStatus;
}
