package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User extends UserManager {

	private Long userId;

	private Client relatedClient;

	private String documentNumber;

	private SystemRole systemRole;

	private String username;

	private String password;
}
