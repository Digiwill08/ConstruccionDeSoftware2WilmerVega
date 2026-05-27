package gestiondeunbanco.wilmervega.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity // <-- ESTA ANOTACIÓN ES OBLIGATORIA PARA JPA
@Table(name = "company_clients") // O el nombre que tenga tu tabla en MySQL
public class CompanyClient {
    @Id
    private String id; // O el tipo de dato que use tu ID
    
    // El resto de los atributos, constructor, getters y setters...
}