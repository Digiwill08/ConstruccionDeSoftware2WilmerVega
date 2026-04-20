package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers-legacy")
@RequiredArgsConstructor
public class ClientManagementController {

    // --- Personas Naturales ---
    
    @PostMapping("/natural/register")
    public ResponseEntity<ClientResponse> registerNaturalClient(@RequestBody NaturalClientRequest request) {
        // TODO: Implementar lógica de registro de personas naturales
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/natural")
    public ResponseEntity<List<ClientResponse>> getAllNaturalClients() {
        // TODO: Implementar obtención de todas las personas naturales
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/natural/{documentNumber}")
    public ResponseEntity<ClientResponse> getNaturalClientByDocumentNumber(@PathVariable String documentNumber) {
        // TODO: Implementar obtención de persona natural por DNI
        return ResponseEntity.ok(new ClientResponse());
    }

    // --- Empresas ---

    @PostMapping("/company/register")
    public ResponseEntity<ClientResponse> registerCompanyClient(@RequestBody CompanyClientRequest request) {
        // TODO: Implementar lógica de registro de empresas
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/company")
    public ResponseEntity<List<ClientResponse>> getAllCompanyClients() {
        // TODO: Implementar obtención de todas las empresas
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/company/{documentNumber}")
    public ResponseEntity<ClientResponse> getCompanyClientByDocumentNumber(@PathVariable String documentNumber) {
        // TODO: Implementar obtención de empresa por NIT
        return ResponseEntity.ok(new ClientResponse());
    }

    // --- Detalles de cliente (para analista) ---

    @GetMapping("/details/{clientId}")
    public ResponseEntity<ClientResponse> getClientDetails(@PathVariable Long clientId) {
        // TODO: Implementar obtención detallada
        return ResponseEntity.ok(new ClientResponse());
    }
}
