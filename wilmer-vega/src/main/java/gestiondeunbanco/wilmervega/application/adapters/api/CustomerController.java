package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.ClientResponse;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.CompanyClientRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.NaturalClientRequest;
import gestiondeunbanco.wilmervega.application.usecases.CustomerUseCase;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    // --- Natural clients ---
    @GetMapping("/natural")
    public ResponseEntity<List<ClientResponse>> getAllNaturalClients() {
        return ResponseEntity.ok(customerUseCase.findAllNaturalClients().stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/natural/{id}")
    public ResponseEntity<ClientResponse> getNaturalClientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toResponse(customerUseCase.findNaturalClientById(id)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/natural/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getNaturalClientByDocument(@PathVariable String documentNumber) {
        try {
            return ResponseEntity.ok(toResponse(customerUseCase.findNaturalClientByDocumentNumber(documentNumber)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/natural")
    public ResponseEntity<?> createNaturalClient(@RequestBody NaturalClientRequest request) {
        try {
            NaturalClient saved = customerUseCase.saveNaturalClient(toNaturalModel(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/natural/{id}")
    public ResponseEntity<?> updateNaturalClient(@PathVariable Long id, @RequestBody NaturalClientRequest request) {
        try {
            NaturalClient model = toNaturalModel(request);
            model.setId(id);
            return ResponseEntity.ok(toResponse(customerUseCase.updateNaturalClient(model)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/natural/{id}")
    public ResponseEntity<Void> deleteNaturalClient(@PathVariable Long id) {
        try {
            customerUseCase.deleteNaturalClientById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Company clients ---
    @GetMapping("/company")
    public ResponseEntity<List<ClientResponse>> getAllCompanyClients() {
        return ResponseEntity.ok(customerUseCase.findAllCompanyClients().stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<ClientResponse> getCompanyClientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toResponse(customerUseCase.findCompanyClientById(id)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/document/{documentNumber}")
    public ResponseEntity<ClientResponse> getCompanyClientByDocument(@PathVariable String documentNumber) {
        try {
            return ResponseEntity.ok(toResponse(customerUseCase.findCompanyClientByDocumentNumber(documentNumber)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/company")
    public ResponseEntity<?> createCompanyClient(@RequestBody CompanyClientRequest request) {
        try {
            CompanyClient saved = customerUseCase.saveCompanyClient(toCompanyModel(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<?> updateCompanyClient(@PathVariable Long id, @RequestBody CompanyClientRequest request) {
        try {
            CompanyClient model = toCompanyModel(request);
            model.setId(id);
            return ResponseEntity.ok(toResponse(customerUseCase.updateCompanyClient(model)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompanyClient(@PathVariable Long id) {
        try {
            customerUseCase.deleteCompanyClientById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
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
