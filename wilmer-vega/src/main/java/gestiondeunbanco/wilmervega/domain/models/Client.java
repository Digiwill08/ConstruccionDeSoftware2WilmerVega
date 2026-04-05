package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public abstract class Client extends Person {

    private String documentNumber;

    
    private List<BankAccount> bankAccounts;

    
    private List<Loan> loans;
}
