package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CompanyClient extends GeneralInformation {

    private String businessName;

    private NaturalClient legalRepresentative;

    private SystemRole role;

    private List<BankAccount> bankAccounts;

    private List<Loan> loans;

    private List<SystemUser> systemUsers;
}
