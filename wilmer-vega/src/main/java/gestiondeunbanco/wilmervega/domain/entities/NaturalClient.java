package gestiondeunbanco.wilmervega.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Period;

/**
 * Natural Client - individual person who is a client
 */
@Entity
@Table(name = "natural_clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaturalClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String identificationNumber;

    private String fullName;

    private LocalDate birthDate;

    private String email;

    private String phone;

    private String address;

    private String occupation;

    private String maritalStatus;

    // Calculate client age
    public int calculateAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Check if client is adult
    public boolean isAdult() {
        return calculateAge() >= 18;
    }

    // Check if client can get credit
    public boolean isEligibleForCredit() {
        return isAdult();
    }
}
