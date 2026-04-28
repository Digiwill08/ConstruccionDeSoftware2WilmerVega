package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private BankAccountEntity sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private BankAccountEntity destinationAccount;

    private BigDecimal amount;
    private LocalDateTime creationDateTime;
    private LocalDateTime approvalDateTime;
    private String transferStatus;
    private Long creatorUserId;
    private Long approverUserId;
}
