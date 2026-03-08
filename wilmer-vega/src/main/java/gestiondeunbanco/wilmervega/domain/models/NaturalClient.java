package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "natural_clients")
@Getter
@Setter
@NoArgsConstructor
public class NaturalClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemRole role;

    @OneToMany(mappedBy = "naturalClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemUser> systemUsers;

    @OneToMany(mappedBy = "naturalClientHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "naturalClientApplicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    @OneToMany(mappedBy = "legalRepresentative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyClient> representedCompanies;
}