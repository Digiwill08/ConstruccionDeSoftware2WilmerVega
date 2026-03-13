package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class User extends Person {

	private Long userId;

	private NaturalClient relatedClient;

	private String fullName;

	private String documentNumber;

	private Date birthDate;

	private SystemRole systemRole;

	private UserStatus userStatus;

	private String username;

	private String password;
}
