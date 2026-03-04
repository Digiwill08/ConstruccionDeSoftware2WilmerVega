package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.AccountType;
import gestiondeunbanco.wilmervega.domain.enums.Currency;
import gestiondeunbanco.wilmervega.domain.enums.AccountStatus;

import jakarta.persistence.Entity;
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
 * Bank Account - savings or checking account
 */
@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String ownerId;

    private BigDecimal currentBalance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDate openingDate;

    // Add money to account
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentBalance = this.currentBalance.add(amount);
        }
    }

    // Remove money from account
    public void withdraw(BigDecimal amount) {
        if (this.currentBalance.compareTo(amount) >= 0) {
            this.currentBalance = this.currentBalance.subtract(amount);
        }
    }

    // Check if account has enough money
    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.currentBalance.compareTo(amount) >= 0;
    }
}
