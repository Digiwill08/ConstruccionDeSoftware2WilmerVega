package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;

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

    public NaturalClient save(NaturalClient naturalClient) {
        if (naturalClient == null) {
            throw new IllegalArgumentException("NaturalClient no puede ser nulo");
        }
        if (naturalClient.getFullName() == null || naturalClient.getFullName().isBlank()) {
            throw new IllegalArgumentException("El nombre completo del cliente natural es obligatorio");
        }

        // Regla 1: ID_Identificacion estrictamente numerico
        String docNum = naturalClient.getDocumentNumber();
        if (docNum == null || docNum.isBlank()) {
            throw new IllegalArgumentException("El numero de documento es obligatorio");
        }
        if (!docNum.matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "El numero de documento debe ser estrictamente numerico. Valor recibido: '" + docNum + "'");
        }

        // Regla 2: Unicidad absoluta del documento en TODOS los tipos de cliente
        if (naturalClientPort.existsByDocumentNumber(docNum)) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente natural con el numero de documento: " + docNum);
        }
        if (companyClientPort.existsByDocumentNumber(docNum)) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente empresa con el numero de documento: " + docNum);
        }

        // Regla 3: Restriccion de Edad (>= 18 anos)
        if (naturalClient.getBirthDate() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento del cliente natural es obligatoria");
        }
        int age = Period.between(naturalClient.getBirthDate(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException(
                    "El cliente debe ser mayor de 18 anos. Edad calculada: " + age + " anos");
        }

        // Campos de contacto obligatorios
        if (naturalClient.getEmail() == null || naturalClient.getEmail().isBlank()
                || !naturalClient.getEmail().contains("@")) {
            throw new IllegalArgumentException("Se requiere un email valido");
        }
        if (naturalClient.getPhone() == null || naturalClient.getPhone().isBlank()) {
            throw new IllegalArgumentException("El telefono es obligatorio");
        }
        if (naturalClient.getAddress() == null || naturalClient.getAddress().isBlank()) {
            throw new IllegalArgumentException("La direccion es obligatoria");
        }
        if (naturalClient.getRole() == null) {
            throw new IllegalArgumentException("El rol del cliente natural es obligatorio");
        }

        return naturalClientPort.save(naturalClient);
    }
}
