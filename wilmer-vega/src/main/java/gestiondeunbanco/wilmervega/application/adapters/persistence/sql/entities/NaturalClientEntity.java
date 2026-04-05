package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "natural_clients")
@Getter
@Setter
@NoArgsConstructor
public class NaturalClientEntity extends ClientEntity {

    private String fullName;
    private LocalDate birthDate;
    private String role;

}
