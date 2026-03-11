package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
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
}
