package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class BankingProduct {
    private Long id;

    private String productCode;
    private String productName;
    private ProductCategory category;
    private Boolean requiresApproval;


}
