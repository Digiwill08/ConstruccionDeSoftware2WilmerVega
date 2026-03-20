package gestiondeunbanco.wilmervega.controller;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.services.CompanyClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-clients")
@RequiredArgsConstructor
public class CompanyClientController {

    private final CompanyClientService companyClientService;

    @GetMapping
    public ResponseEntity<List<CompanyClient>> getAll() {
        return ResponseEntity.ok(companyClientService.getAllCompanyClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyClient> getById(@PathVariable Long id) {
        return companyClientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompanyClient> create(@RequestBody CompanyClient companyClient) {
        try {
            CompanyClient saved = companyClientService.save(companyClient);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyClientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}