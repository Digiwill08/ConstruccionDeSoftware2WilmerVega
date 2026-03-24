package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class NaturalClient extends Person {

    private String fullName;

    private String documentNumber;

    private Date birthDate;

    private SystemRole role;

    @Transient
    private List<SystemUser> systemUsers;

    @Transient
    private List<BankAccount> bankAccounts;

    @Transient
    private List<Loan> loans;

    @Transient
    private List<CompanyClient> representedCompanies;

}
