package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Date;

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
    private Date approvalDate;
    private Date disbursementDate;
    
    @ManyToOne
    @JoinColumn(name = "natural_client_id")
    private NaturalClientEntity naturalClientApplicant;

    @ManyToOne
    @JoinColumn(name = "company_client_id")
    private CompanyClientEntity companyClientApplicant;
}
