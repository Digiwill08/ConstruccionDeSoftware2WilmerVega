package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class NaturalClient {

    private Long id;

    private String fullName;

    private String documentNumber;

    private String email;

    private String phone;

    private String address;

    private Date birthDate;
<<<<<<< Updated upstream
}
=======

    private SystemRole role;

    private List<SystemUser> systemUsers;

    private List<BankAccount> bankAccounts;

    private List<Loan> loans;

    @OneToMany(mappedBy = "legalRepresentative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyClient> representedCompanies;


}
>>>>>>> Stashed changes
