package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserManager extends SystemUser {

    private User user;

    private List<SystemUser> managedUsers;

    private List<CompanyClient> managedCompanies;

    private List<NaturalClient> managedNaturalClients;
}
