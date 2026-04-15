package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String type; // "NATURAL" o "COMPANY"
    private String documentNumber;
    private String email;
    private String phone;
    private String address;
    private String fullName; // para persona natural
    private LocalDate birthDate; // para persona natural
    private String businessName; // para empresa
    private Long legalRepresentativeId; // para empresa
    private String status;
}
