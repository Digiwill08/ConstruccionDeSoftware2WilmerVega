package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private String loanType;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private String loanStatus;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private Long approvedByUserId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clientApplicant;

    @ManyToOne
    @JoinColumn(name = "disbursement_account_id")
    private BankAccountEntity disbursementAccount;
}
