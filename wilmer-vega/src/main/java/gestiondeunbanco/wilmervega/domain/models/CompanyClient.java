package gestiondeunbanco.wilmervega.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CompanyClient extends NaturalClient {

    private String businessName;

    private NaturalClient legalRepresentative;
}
