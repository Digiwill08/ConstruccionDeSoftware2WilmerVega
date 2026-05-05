package gestiondeunbanco.wilmervega.application.adapters.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyClientRequest {
    @NotBlank(message = "Business name is required")
    @Size(min = 3, max = 150, message = "Business name must be between 3 and 150 characters")
    private String businessName;

    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "Document must be 6-20 numeric digits")
    private String documentNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email format is required")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must be 7-15 digits, optionally starting with +")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotNull(message = "Legal representative ID is required")
    private Long legalRepresentativeId;
}
