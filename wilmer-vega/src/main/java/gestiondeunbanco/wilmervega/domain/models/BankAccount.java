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

    private BankingProduct bankingProduct;

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

    public void validateState() {
        if (currentBalance == null) {
            throw new IllegalStateException("Current balance is required");
        }

        if (currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Current balance cannot be negative");
        }

        if (!hasSingleHolder()) {
            throw new IllegalStateException("Bank account must have exactly one holder");
        }
    }

    public boolean hasSingleHolder() {
        boolean hasNaturalHolder = naturalClientHolder != null;
        boolean hasCompanyHolder = companyClientHolder != null;
        return hasNaturalHolder ^ hasCompanyHolder;
    }
}
