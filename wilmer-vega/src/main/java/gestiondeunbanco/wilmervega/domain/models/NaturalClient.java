package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class NaturalClient {
  
    private Long id;

    private String fullName;

    private String documentNumber;

    private String email;

    private String phone;

    private String address;

    private Date birthDate;

}