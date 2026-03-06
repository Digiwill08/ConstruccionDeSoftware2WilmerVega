package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_clients")
@Getter
@Setter
@NoArgsConstructor
public class CompanyClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String businessName;

    @Column(nullable = false, unique = true, length = 20)
    private String taxId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 200)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_representative_id", nullable = false)
    private NaturalClient legalRepresentative;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemRole role;

    @OneToMany(mappedBy = "companyClientHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "companyClientApplicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    @OneToMany(mappedBy = "companyClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemUser> systemUsers;


}
