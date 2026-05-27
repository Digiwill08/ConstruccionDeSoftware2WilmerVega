package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalClientRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "El numero de documento es obligatorio")
    @Pattern(regexp = "^\\d+$", message = "El numero de documento debe ser estrictamente numerico")
    private String documentNumber;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es invalido")
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    private String phone;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate birthDate;

    @NotBlank(message = "La direccion es obligatoria")
    private String address;

    // Opcional: default NATURAL_CLIENT si se omite
    private String role;
}
