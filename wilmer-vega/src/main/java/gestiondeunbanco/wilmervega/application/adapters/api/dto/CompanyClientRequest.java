package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyClientRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String businessName;

    @NotBlank(message = "El numero de documento es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El numero de documento debe contener solo numeros")
    private String documentNumber;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es invalido")
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    private String phone;

    @NotBlank(message = "La direccion es obligatoria")
    private String address;

    @NotNull(message = "El representante legal es obligatorio")
    private Long legalRepresentativeId;
}
