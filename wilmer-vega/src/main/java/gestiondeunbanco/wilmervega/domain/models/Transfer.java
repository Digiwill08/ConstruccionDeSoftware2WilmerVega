package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {
<<<<<<< Updated upstream
    private int transferId;
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private double amount;
    private Date creationDateTime;
    private Date approvalDateTime;
    private TransferStatus transferStatus;
    private SystemUser creatorUser;
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    private BankAccount sourceAccount;

    private BankAccount destinationAccount;

    private BigDecimal amount;

    private Timestamp creationDateTime;

    private Timestamp approvalDateTime;

    private TransferStatus transferStatus;

    private SystemUser creatorUser;

>>>>>>> Stashed changes
    private SystemUser approverUser;
}
