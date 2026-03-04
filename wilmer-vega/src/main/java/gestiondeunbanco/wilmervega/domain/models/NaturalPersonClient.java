package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
public class NaturalPersonClient {

    private Long id;

    private String fullName;
    private String documentNumber;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    
    private SystemRole role;
    
    private String passwordHash;

    public NaturalPersonClient(String fullName, String documentNumber, String email, 
                        String phone, String address, LocalDate birthDate, 
                        String passwordHash) {
        validateBirthDate(birthDate);
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.role = SystemRole.NATURAL_CLIENT;
        this.passwordHash = passwordHash;
    }

    public int calculateAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public boolean isAdult() { 
        return calculateAge() >= 18; 
    }

    private void validateBirthDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Birth date is required.");
        if (date.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Birth date cannot be in the future.");
        if (Period.between(date, LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("Client must be at least 18 years old.");
    }

    public void setBirthDate(LocalDate date) {
        validateBirthDate(date);
        this.birthDate = date;
    }
}
