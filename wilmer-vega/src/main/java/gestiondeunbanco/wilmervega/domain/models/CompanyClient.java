package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
@Getter
@Setter
@NoArgsConstructor
public class CompanyClient {
    private Long id;

    private String businessName;

<<<<<<< Updated upstream
=======
    private String taxId;

>>>>>>> Stashed changes
    private String email;

    private String phone;

    private String address;

    private NaturalClient legalRepresentative;

    private SystemRole role;

    private List<BankAccount> bankAccounts;

    private List<Loan> loans;

    private List<SystemUser> systemUsers;
}
