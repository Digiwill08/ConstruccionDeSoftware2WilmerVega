package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    private int transferId;
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private double amount;
    private Date creationDateTime;
    private Date approvalDateTime;
    private TransferStatus transferStatus;
    private SystemUser creatorUser;
    private SystemUser approverUser;
}
