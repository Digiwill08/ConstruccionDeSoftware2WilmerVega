package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.application.usecases.CompanyClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-clients")
@RequiredArgsConstructor
public class CompanyClientController {

    private final CompanyClientUseCase companyClientUseCase;

    @GetMapping
    public ResponseEntity<List<CompanyClient>> getAll() {
        return ResponseEntity.ok(companyClientUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyClient> getById(@PathVariable Long id) {
        return companyClientUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompanyClient> create(@RequestBody CompanyClient companyClient) {
        try {
            CompanyClient saved = companyClientUseCase.save(companyClient);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyClientUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
