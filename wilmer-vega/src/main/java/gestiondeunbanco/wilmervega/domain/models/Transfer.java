package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    private Long transferId;

    private BankAccount sourceAccount;

    private BankAccount destinationAccount;

    private BigDecimal amount;

    private Date creationDateTime;

    private Date approvalDateTime;

    private TransferStatus transferStatus;

    private SystemUser creatorUser;
    private SystemUser approverUser;
}
