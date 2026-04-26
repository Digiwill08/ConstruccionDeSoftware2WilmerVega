package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.ClientResponse;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.CompanyClientRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.NaturalClientRequest;
import gestiondeunbanco.wilmervega.application.usecases.CustomerUseCase;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    // --- Natural clients ---
    @GetMapping("/natural")
    public ResponseEntity<List<ClientResponse>> getAllNaturalClients() {
        return ResponseEntity.ok(customerUseCase.findAllNaturalClients().stream().map(this::toResponse).toList());
    }

    @GetMapping("/natural/{id}")
    public ResponseEntity<ClientResponse> getNaturalClientById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(customerUseCase.findNaturalClientById(id)));
    }

    @GetMapping("/natural/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getNaturalClientByDocument(@PathVariable String documentNumber) {
        return ResponseEntity.ok(toResponse(customerUseCase.findNaturalClientByDocumentNumber(documentNumber)));
    }

    @PostMapping("/natural")
    public ResponseEntity<Map<String, Object>> createNaturalClient(@RequestBody NaturalClientRequest request) {
        NaturalClient saved = customerUseCase.saveNaturalClient(toNaturalModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Cliente natural creado correctamente",
                "id", saved.getId(),
                "type", "NATURAL",
                "documentNumber", saved.getDocumentNumber()
        ));
    }

    @PutMapping("/natural/{id}")
    public ResponseEntity<ClientResponse> updateNaturalClient(@PathVariable Long id, @RequestBody NaturalClientRequest request) {
        NaturalClient model = toNaturalModel(request);
        model.setId(id);
        return ResponseEntity.ok(toResponse(customerUseCase.updateNaturalClient(model)));
    }

    @DeleteMapping("/natural/{id}")
    public ResponseEntity<Void> deleteNaturalClient(@PathVariable Long id) {
        customerUseCase.deleteNaturalClientById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Company clients ---
    @GetMapping("/company")
    public ResponseEntity<List<ClientResponse>> getAllCompanyClients() {
        return ResponseEntity.ok(customerUseCase.findAllCompanyClients().stream().map(this::toResponse).toList());
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<ClientResponse> getCompanyClientById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(customerUseCase.findCompanyClientById(id)));
    }

    @GetMapping("/company/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getCompanyClientByDocument(@PathVariable String documentNumber) {
        return ResponseEntity.ok(toResponse(customerUseCase.findCompanyClientByDocumentNumber(documentNumber)));
    }

    @PostMapping("/company")
    public ResponseEntity<Map<String, Object>> createCompanyClient(@RequestBody CompanyClientRequest request) {
        CompanyClient saved = customerUseCase.saveCompanyClient(toCompanyModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Cliente empresa creado correctamente",
                "id", saved.getId(),
                "type", "COMPANY",
                "documentNumber", saved.getDocumentNumber()
        ));
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<ClientResponse> updateCompanyClient(@PathVariable Long id, @RequestBody CompanyClientRequest request) {
        CompanyClient model = toCompanyModel(request);
        model.setId(id);
        return ResponseEntity.ok(toResponse(customerUseCase.updateCompanyClient(model)));
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompanyClient(@PathVariable Long id) {
        customerUseCase.deleteCompanyClientById(id);
        return ResponseEntity.noContent().build();
    }

    private NaturalClient toNaturalModel(NaturalClientRequest request) {
        NaturalClient model = new NaturalClient();
        model.setDocumentNumber(request.getDocumentNumber());
        model.setEmail(request.getEmail());
        model.setPhone(request.getPhone());
        model.setAddress(request.getAddress());
        model.setFullName(request.getFullName());
        model.setBirthDate(request.getBirthDate());
        if (request.getRole() != null && !request.getRole().isBlank()) {
            model.setRole(SystemRole.valueOf(request.getRole().trim().toUpperCase(Locale.ROOT)));
        } else {
            model.setRole(SystemRole.NATURAL_CLIENT);
        }
        return model;
    }

    private CompanyClient toCompanyModel(CompanyClientRequest request) {
        CompanyClient model = new CompanyClient();
        model.setDocumentNumber(request.getDocumentNumber());
        model.setEmail(request.getEmail());
        model.setPhone(request.getPhone());
        model.setAddress(request.getAddress());
        model.setBusinessName(request.getBusinessName());
        if (request.getLegalRepresentativeId() != null) {
            NaturalClient representative = new NaturalClient();
            representative.setId(request.getLegalRepresentativeId());
            model.setLegalRepresentative(representative);
        }
        return model;
    }

    private ClientResponse toResponse(NaturalClient model) {
        return new ClientResponse(
                model.getId(),
                "NATURAL",
                model.getDocumentNumber(),
                model.getEmail(),
                model.getPhone(),
                model.getAddress(),
                model.getFullName(),
                model.getBirthDate(),
                model.getRole() != null ? model.getRole().name() : null,
                null,
                null,
                null
        );
    }

    private ClientResponse toResponse(CompanyClient model) {
        return new ClientResponse(
                model.getId(),
                "COMPANY",
                model.getDocumentNumber(),
                model.getEmail(),
                model.getPhone(),
                model.getAddress(),
                null,
                null,
                null,
                model.getBusinessName(),
                model.getLegalRepresentative() != null ? model.getLegalRepresentative().getId() : null,
                null
        );
    }
}
