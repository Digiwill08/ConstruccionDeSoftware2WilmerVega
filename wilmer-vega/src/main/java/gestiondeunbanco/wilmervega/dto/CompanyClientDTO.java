package gestiondeunbanco.wilmervega.dto;

import lombok.Data;

@Data
public class CompanyClientDTO {
    private String businessName;
    private String taxId;
    private String email;
    private String phone;
    private String address;
    private Long legalRepresentativeId;
}