package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.LoanType;
import gestiondeunbanco.wilmervega.domain.enums.LoanStatus;

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
import java.time.LocalDate;

/**
 * Loan - credit given to clients
 */
@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private String applicantClientId;

    private BigDecimal requestedAmount;

    private BigDecimal approvedAmount;

    private BigDecimal interestRate;

    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private LocalDate approvalDate;

    private LocalDate disbursementDate;

    private String disbursementAccountNumber;

    private Long approverAnalystId;

    // Approve the loan
    public void approve(BigDecimal approvedAmount, Long analystId) {
        this.approvedAmount = approvedAmount;
        this.loanStatus = LoanStatus.APPROVED;
        this.approvalDate = LocalDate.now();
        this.approverAnalystId = analystId;
    }

    // Reject the loan
    public void reject(Long analystId) {
        this.loanStatus = LoanStatus.REJECTED;
        this.approvalDate = LocalDate.now();
        this.approverAnalystId = analystId;
    }

    // Disburse (give money) the loan
    public void disburse(String accountNumber) {
        this.disbursementAccountNumber = accountNumber;
        this.loanStatus = LoanStatus.DISBURSED;
        this.disbursementDate = LocalDate.now();
    }
}
