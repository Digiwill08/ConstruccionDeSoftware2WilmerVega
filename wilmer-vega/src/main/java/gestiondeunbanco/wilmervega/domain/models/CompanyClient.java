package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class CompanyClient {
    private Long id;

    private String businessName;
    private String taxId;
    private String email;
    private String phone;
    private String address;
    private Long legalRepresentativeId;
    private SystemRole role;


}
