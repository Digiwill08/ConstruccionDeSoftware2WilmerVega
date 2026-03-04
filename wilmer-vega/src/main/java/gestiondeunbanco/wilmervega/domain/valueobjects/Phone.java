package gestiondeunbanco.wilmervega.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Phone - represents a phone number
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    private static final int MIN_LENGTH = 7;
    private static final int MAX_LENGTH = 15;

    private String value;

    private Phone(String value) {
        validate(value);
        this.value = value;
    }

    public static Phone of(String value) {
        return new Phone(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        
        // Remove spaces, hyphens and parentheses to count only digits
        String digits = value.replaceAll("[\\s\\-\\(\\)]", "");
        
        if (digits.length() < MIN_LENGTH || digits.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Phone must have between %d and %d digits. Received: %d", 
                    MIN_LENGTH, MAX_LENGTH, digits.length())
            );
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
