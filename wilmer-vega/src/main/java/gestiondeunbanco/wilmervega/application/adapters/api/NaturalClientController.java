package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.application.usecases.NaturalClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/natural-clients")
@RequiredArgsConstructor
public class NaturalClientController {

    private final NaturalClientUseCase naturalClientUseCase;

    @GetMapping
    public ResponseEntity<List<NaturalClient>> getAll() {
        return ResponseEntity.ok(naturalClientUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NaturalClient> getById(@PathVariable Long id) {
        return naturalClientUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<NaturalClient> getByDocumentNumber(@PathVariable String documentNumber) {
        return naturalClientUseCase.findByDocumentNumber(documentNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NaturalClient> create(@RequestBody NaturalClient naturalClient) {
        try {
            NaturalClient saved = naturalClientUseCase.save(naturalClient);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        naturalClientUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
