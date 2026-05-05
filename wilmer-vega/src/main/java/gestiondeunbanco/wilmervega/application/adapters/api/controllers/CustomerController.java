package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.ClientResponse;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.CompanyClientRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.NaturalClientRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.mappers.CompanyClientMapper;
import gestiondeunbanco.wilmervega.application.adapters.api.mappers.NaturalClientMapper;
import gestiondeunbanco.wilmervega.application.usecases.CustomerUseCase;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.services.ClientAccessValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;
    private final NaturalClientMapper naturalClientMapper;
    private final CompanyClientMapper companyClientMapper;
    private final ClientAccessValidationService clientAccessValidation;

    // --- Natural clients ---
    @GetMapping("/natural")
    public ResponseEntity<List<ClientResponse>> getAllNaturalClients() {
        return ResponseEntity.ok(customerUseCase.findAllNaturalClients().stream().map(this::toResponse).toList());
    }

    @GetMapping("/natural/{id}")
    public ResponseEntity<ClientResponse> getNaturalClientById(@PathVariable Long id) {
        clientAccessValidation.validateClientAccess(id);
        return ResponseEntity.ok(toResponse(customerUseCase.findNaturalClientById(id)));
    }

    @GetMapping("/natural/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getNaturalClientByDocument(@PathVariable String documentNumber) {
        NaturalClient client = customerUseCase.findNaturalClientByDocumentNumber(documentNumber);
        clientAccessValidation.validateClientAccess(client.getId());
        return ResponseEntity.ok(toResponse(client));
    }

    @PostMapping("/natural")
    public ResponseEntity<Map<String, Object>> createNaturalClient(@Valid @RequestBody NaturalClientRequest request) {
        NaturalClient saved = customerUseCase.saveNaturalClient(naturalClientMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Cliente natural creado correctamente",
                "id", saved.getId(),
                "type", "NATURAL",
                "documentNumber", saved.getDocumentNumber()
        ));
    }

    @PutMapping("/natural/{id}")
    public ResponseEntity<ClientResponse> updateNaturalClient(@PathVariable Long id, @Valid @RequestBody NaturalClientRequest request) {
        clientAccessValidation.validateClientAccess(id);
        NaturalClient model = naturalClientMapper.toModel(request);
        model.setId(id);
        return ResponseEntity.ok(toResponse(customerUseCase.updateNaturalClient(model)));
    }

    @DeleteMapping("/natural/{id}")
    public ResponseEntity<Void> deleteNaturalClient(@PathVariable Long id) {
        clientAccessValidation.validateClientAccess(id);
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
        clientAccessValidation.validateClientAccess(id);
        return ResponseEntity.ok(toResponse(customerUseCase.findCompanyClientById(id)));
    }

    @GetMapping("/company/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getCompanyClientByDocument(@PathVariable String documentNumber) {
        CompanyClient client = customerUseCase.findCompanyClientByDocumentNumber(documentNumber);
        clientAccessValidation.validateClientAccess(client.getId());
        return ResponseEntity.ok(toResponse(client));
    }

    @PostMapping("/company")
    public ResponseEntity<Map<String, Object>> createCompanyClient(@Valid @RequestBody CompanyClientRequest request) {
        CompanyClient saved = customerUseCase.saveCompanyClient(companyClientMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Cliente empresa creado correctamente",
                "id", saved.getId(),
                "type", "COMPANY",
                "documentNumber", saved.getDocumentNumber()
        ));
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<ClientResponse> updateCompanyClient(@PathVariable Long id, @Valid @RequestBody CompanyClientRequest request) {
        clientAccessValidation.validateClientAccess(id);
        CompanyClient model = companyClientMapper.toModel(request);
        model.setId(id);
        return ResponseEntity.ok(toResponse(customerUseCase.updateCompanyClient(model)));
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompanyClient(@PathVariable Long id) {
        clientAccessValidation.validateClientAccess(id);
        customerUseCase.deleteCompanyClientById(id);
        return ResponseEntity.noContent().build();
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
