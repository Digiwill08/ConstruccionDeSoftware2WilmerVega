package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
<<<<<<< Updated upstream
//jdjdj
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private NaturalClient naturalClientHolder;

    private CompanyClient companyClientHolder;

    private BigDecimal currentBalance;

    private Currency currency;

    private AccountStatus accountStatus;

    private Date openingDate;

    private List<Transfer> outgoingTransfers;

    private List<Transfer> incomingTransfers;


>>>>>>> Stashed changes
}
