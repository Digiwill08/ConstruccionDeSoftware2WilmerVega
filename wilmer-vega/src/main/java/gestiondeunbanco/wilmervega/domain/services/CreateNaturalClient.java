package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.ClientUnderageException;
import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidContactInformationException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidNaturalClientException;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

/**
 * Servicio de dominio para crear clientes Persona Natural.
 * Reglas de seguridad (Blindaje de Identidad):
 *  1. documentNumber debe ser estrictamente numerico
 *  2. documentNumber unico en TODOS los tipos de cliente
 *  3. Cliente debe ser mayor de 18 años
 */
public class CreateNaturalClient {

    private final NaturalClientPort naturalClientPort;
    private final CompanyClientPort companyClientPort;

    public CreateNaturalClient(NaturalClientPort naturalClientPort, CompanyClientPort companyClientPort) {
        this.naturalClientPort = naturalClientPort;
        this.companyClientPort = companyClientPort;
    }

    @Transactional
    public NaturalClient save(NaturalClient naturalClient) {
        if (naturalClient == null) {
            throw new InvalidNaturalClientException("NaturalClient no puede ser nulo");
        }
        if (naturalClient.getFullName() == null || naturalClient.getFullName().isBlank()) {
            throw new InvalidNaturalClientException("El nombre completo del cliente natural es obligatorio");
        }

        // Regla 1: ID_Identificacion estrictamente numerico
        String docNum = naturalClient.getDocumentNumber();
        if (docNum == null || docNum.isBlank()) {
            throw new InvalidDocumentNumberException("El numero de documento es obligatorio");
        }
        if (!docNum.matches("^\\d+$")) {
            throw new InvalidDocumentNumberException(
                    "El numero de documento debe ser estrictamente numerico. Valor recibido: '" + docNum + "'");
        }

        // Regla 2: Unicidad absoluta del documento en TODOS los tipos de cliente
        if (naturalClientPort.existsByDocumentNumber(docNum)) {
            throw new DuplicateDocumentNumberException(
                    "Ya existe un cliente natural con el numero de documento: " + docNum);
        }
        if (companyClientPort.existsByDocumentNumber(docNum)) {
            throw new DuplicateDocumentNumberException(
                    "Ya existe un cliente empresa con el numero de documento: " + docNum);
        }

        // Regla 3: Restriccion de Edad (>= 18 anos)
        if (naturalClient.getBirthDate() == null) {
            throw new InvalidNaturalClientException("La fecha de nacimiento del cliente natural es obligatoria");
        }
        int age = Period.between(naturalClient.getBirthDate(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new ClientUnderageException(
                    "El cliente debe ser mayor de 18 anos. Edad calculada: " + age + " anos");
        }

        // Campos de contacto obligatorios
        if (naturalClient.getEmail() == null || naturalClient.getEmail().isBlank()
                || !naturalClient.getEmail().contains("@")) {
            throw new InvalidContactInformationException("Se requiere un email valido");
        }
        if (naturalClient.getPhone() == null || naturalClient.getPhone().isBlank()) {
            throw new InvalidContactInformationException("El telefono es obligatorio");
        }
        if (naturalClient.getAddress() == null || naturalClient.getAddress().isBlank()) {
            throw new InvalidContactInformationException("La direccion es obligatoria");
        }
        if (naturalClient.getRole() == null) {
            throw new InvalidNaturalClientException("El rol del cliente natural es obligatorio");
        }

        return naturalClientPort.save(naturalClient);
    }
}
