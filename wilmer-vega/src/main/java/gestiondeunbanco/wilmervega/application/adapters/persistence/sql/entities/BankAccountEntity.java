package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private String accountType;

    private BigDecimal currentBalance;

    private String currency;

    private String accountStatus;

    private LocalDate openingDate;

    // Simplificado
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity holder;
}
