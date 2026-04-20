package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalClientRequest {
    private String fullName;
    private String documentNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String role;
}
