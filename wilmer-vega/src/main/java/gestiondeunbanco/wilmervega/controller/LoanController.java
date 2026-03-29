package gestiondeunbanco.wilmervega.controller;

import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.application.usecases.LoanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanUseCase loanUseCase;

    @GetMapping
    public ResponseEntity<List<Loan>> getAll() {
        return ResponseEntity.ok(loanUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable Long id) {
        return loanUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody Loan loan) {
        try {
            Loan saved = loanUseCase.save(loan);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
