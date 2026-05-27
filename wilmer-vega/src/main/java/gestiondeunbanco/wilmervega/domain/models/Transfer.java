package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    private Long transferId;

    private BankAccount sourceAccount;

    private BankAccount destinationAccount;

    private BigDecimal amount;

    private LocalDateTime creationDateTime;

    private LocalDateTime approvalDateTime;

    private TransferStatus transferStatus;

    private Long creatorUserId;

    private Long approverUserId;
}
