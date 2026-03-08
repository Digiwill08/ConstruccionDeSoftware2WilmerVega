package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "banking_products")
@Getter
@Setter
@NoArgsConstructor
public class BankingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String productCode;

    @Column(nullable = false, length = 100)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private Boolean requiresApproval;
}
