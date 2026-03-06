package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
    private Long id;

    private String accountNumber;
    private AccountType accountType;
    private String accountHolderId;
    private BigDecimal currentBalance;
    private Currency currency;
    private AccountStatus accountStatus;
    private Date openingDate;


}
