package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NaturalClient extends Usuario {

    private String fullName;

    private String documentNumber;

    private Date birthDate;

    private SystemRole role;

    private List<SystemUser> systemUsers;

    private List<BankAccount> bankAccounts;

    private List<Loan> loans;

    private List<CompanyClient> representedCompanies;

}
