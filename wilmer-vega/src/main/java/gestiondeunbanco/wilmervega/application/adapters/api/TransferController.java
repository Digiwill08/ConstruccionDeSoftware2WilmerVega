package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.application.usecases.TransferUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferUseCase transferUseCase;

    @GetMapping
    public ResponseEntity<List<Transfer>> getAll() {
        return ResponseEntity.ok(transferUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getById(@PathVariable Long id) {
        return transferUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transfer> create(@RequestBody Transfer transfer) {
        try {
            Transfer saved = transferUseCase.save(transfer);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transferUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
