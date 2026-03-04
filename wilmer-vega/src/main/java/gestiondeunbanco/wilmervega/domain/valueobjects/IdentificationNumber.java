package gestiondeunbanco.wilmervega.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Identification Number - represents an ID number
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdentificationNumber {

    private String value;

    private IdentificationNumber(String value) {
        validate(value);
        this.value = value;
    }

    public static IdentificationNumber of(String value) {
        return new IdentificationNumber(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Identification number cannot be empty.");
        }
        if (value.length() > 20) {
            throw new IllegalArgumentException("Identification number cannot exceed 20 characters.");
        }
        // Remove spaces and hyphens to validate only numbers/letters
        String clean = value.replaceAll("[\\s-]", "");
        if (clean.isEmpty()) {
            throw new IllegalArgumentException("Identification number must contain valid characters.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
