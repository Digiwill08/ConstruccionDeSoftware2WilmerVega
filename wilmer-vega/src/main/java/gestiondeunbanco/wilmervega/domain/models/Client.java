package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public abstract class Client extends Person {

    private String documentNumber;

    @Transient
    private List<BankAccount> bankAccounts;

    @Transient
    private List<Loan> loans;
}
