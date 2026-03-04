package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.ProductCategory;

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

/**
 * General catalog of banking products and services.
 */
@Entity
@Table(name = "bank_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankProduct {

    @Id
    private String productCode;

    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Boolean requiresApproval;

    private String description;
}
