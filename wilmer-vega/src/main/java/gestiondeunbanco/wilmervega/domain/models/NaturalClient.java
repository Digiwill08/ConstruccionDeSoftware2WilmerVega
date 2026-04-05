package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class NaturalClient extends Client {

    private String fullName;

    private LocalDate birthDate;

    private SystemRole role;

    
    private List<SystemUser> systemUsers;

    
    private List<CompanyClient> representedCompanies;

}
