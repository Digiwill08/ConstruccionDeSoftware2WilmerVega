package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    private Long transferId;

    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private Timestamp creationDateTime;
    private Timestamp approvalDateTime;
    private TransferStatus transferStatus;
    private Long creatorUserId;
    private Long approverUserId;


}
