package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyClientRequest {
    private String businessName;
    private String documentNumber;
    private String email;
    private String phone;
    private String address;
    private Long legalRepresentativeId;
}
