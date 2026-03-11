package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralInformation {

    private Long id;

    private String email;

    private String phone;

    private String address;
}
