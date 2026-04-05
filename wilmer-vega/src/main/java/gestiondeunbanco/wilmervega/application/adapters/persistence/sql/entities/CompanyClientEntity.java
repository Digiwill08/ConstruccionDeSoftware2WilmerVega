package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company_clients")
@Getter
@Setter
@NoArgsConstructor
public class CompanyClientEntity extends ClientEntity {

    private String businessName;
    
    @ManyToOne
    @JoinColumn(name = "legal_rep_id")
    private NaturalClientEntity legalRepresentative;
}
