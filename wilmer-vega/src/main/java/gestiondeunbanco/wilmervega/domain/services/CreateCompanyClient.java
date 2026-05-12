package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidCompanyClientException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidContactInformationException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.springframework.transaction.annotation.Transactional;

public class CreateCompanyClient {

    private final CompanyClientPort companyClientPort;
    private final NaturalClientPort naturalClientPort;

    public CreateCompanyClient(CompanyClientPort companyClientPort, NaturalClientPort naturalClientPort) {
        this.companyClientPort = companyClientPort;
        this.naturalClientPort = naturalClientPort;
    }

    @Transactional
    public CompanyClient save(CompanyClient companyClient) {
        if (companyClient == null) {
            throw new InvalidCompanyClientException("CompanyClient no puede ser nulo");
        }
        if (companyClient.getBusinessName() == null || companyClient.getBusinessName().isBlank()) {
            throw new InvalidCompanyClientException("La razon social de la empresa es obligatoria");
        }
        if (companyClient.getEmail() == null || companyClient.getEmail().isBlank()
                || !companyClient.getEmail().contains("@")) {
            throw new InvalidContactInformationException("Se requiere un email valido");
        }
        if (companyClient.getPhone() == null || companyClient.getPhone().isBlank()) {
            throw new InvalidContactInformationException("El telefono es obligatorio");
        }
        if (companyClient.getAddress() == null || companyClient.getAddress().isBlank()) {
            throw new InvalidContactInformationException("La direccion es obligatoria");
        }

        // ID_Identificacion: obligatorio y estrictamente numerico
        String docNum = companyClient.getDocumentNumber();
        if (docNum == null || docNum.isBlank()) {
            throw new InvalidDocumentNumberException("El numero de documento de la empresa es obligatorio");
        }
        if (!docNum.matches("^\\d+$")) {
            throw new InvalidDocumentNumberException(
                    "El numero de documento debe ser estrictamente numerico. Valor recibido: '" + docNum + "'");
        }

        // Unicidad absoluta entre todos los tipos de cliente
        if (companyClientPort.existsByDocumentNumber(docNum)) {
            throw new DuplicateDocumentNumberException("Ya existe una empresa con el numero de documento: " + docNum);
        }
        if (naturalClientPort.existsByDocumentNumber(docNum)) {
            throw new DuplicateDocumentNumberException("Ya existe un cliente natural con el numero de documento: " + docNum);
        }

        // Representante legal obligatorio y existente
        if (companyClient.getLegalRepresentative() == null
                || companyClient.getLegalRepresentative().getId() == null) {
            throw new InvalidCompanyClientException("El representante legal es obligatorio");
        }
        naturalClientPort.findById(companyClient.getLegalRepresentative().getId())
                .orElseThrow(() -> new NotFoundException(
                        "Representante legal no encontrado con ID: "
                        + companyClient.getLegalRepresentative().getId()));

        return companyClientPort.save(companyClient);
    }
}
