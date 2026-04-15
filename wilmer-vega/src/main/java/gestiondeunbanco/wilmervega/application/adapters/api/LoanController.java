package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    @PostMapping
    public ResponseEntity<LoanResponse> requestLoan(@RequestBody LoanRequest request) {
        // TODO: Implementar solicitud de préstamo
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getMyLoans() {
        // TODO: Implementar obtención de préstamos del cliente
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long loanId) {
        // TODO: Implementar obtención de préstamo por ID
        return ResponseEntity.ok(new LoanResponse());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanResponse>> getLoansByStatus(@PathVariable String status) {
        // TODO: Implementar obtención de préstamos por estado
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/approve")
    public ResponseEntity<LoanResponse> approveLoan(@RequestBody LoanApprovalRequest request) {
        // TODO: Implementar aprobación de préstamo (solo ANALYST)
        return ResponseEntity.ok(new LoanResponse());
    }

    @PostMapping("/reject")
    public ResponseEntity<LoanResponse> rejectLoan(@RequestBody LoanApprovalRequest request) {
        // TODO: Implementar rechazo de préstamo (solo ANALYST)
        return ResponseEntity.ok(new LoanResponse());
    }

    @PostMapping("/disburse")
    public ResponseEntity<LoanResponse> disburseLoan(@RequestBody LoanApprovalRequest request) {
        // TODO: Implementar desembolso de préstamo (solo ANALYST)
        return ResponseEntity.ok(new LoanResponse());
    }
}
