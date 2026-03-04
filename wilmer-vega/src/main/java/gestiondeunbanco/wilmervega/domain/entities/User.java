package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.SystemRole;
import gestiondeunbanco.wilmervega.domain.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Period;

/**
 * User class - represents people who use the system
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String relatedId;

    private String fullName;

    private String identificationId;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private String address;

    private SystemRole systemRole;

    private UserStatus userStatus;

    private String passwordHash;

    // Calculate user age
    public int calculateAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Check if user is adult (18+)
    public boolean isAdult() {
        return calculateAge() >= 18;
    }
}
