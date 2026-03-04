package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.TransferStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Transfer - money movement between accounts
 */
@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    private String sourceAccount;

    private String destinationAccount;

    private BigDecimal amount;

    private LocalDateTime creationDate;

    private LocalDateTime approvalDate;

    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    private Long creatorUserId;

    private Long approverUserId;

    private Boolean requiresApproval;

    // Approve the transfer
    public void approve(Long approverId) {
        this.transferStatus = TransferStatus.APPROVED;
        this.approvalDate = LocalDateTime.now();
        this.approverUserId = approverId;
    }

    // Reject the transfer
    public void reject(Long approverId) {
        this.transferStatus = TransferStatus.REJECTED;
        this.approvalDate = LocalDateTime.now();
        this.approverUserId = approverId;
    }

    // Execute (do) the transfer
    public void execute() {
        this.transferStatus = TransferStatus.EXECUTED;
    }

    // Check if transfer is expired
    public boolean isExpired() {
        if (this.transferStatus == TransferStatus.AWAITING_APPROVAL) {
            long minutesElapsed = ChronoUnit.MINUTES.between(creationDate, LocalDateTime.now());
            return minutesElapsed > 60;
        }
        return false;
    }
}
