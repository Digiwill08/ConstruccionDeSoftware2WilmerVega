package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    @PostMapping
    public ResponseEntity<BankAccountResponse> createBankAccount(@RequestBody BankAccountRequest request) {
        // TODO: Implementar creación de cuenta bancaria
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllMyAccounts() {
        // TODO: Implementar obtención de cuentas del cliente autenticado
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        // TODO: Implementar obtención de cuenta por número
        return ResponseEntity.ok(new BankAccountResponse());
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable Long accountId) {
        // TODO: Implementar obtención de saldo
        return ResponseEntity.ok(new Object());
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<BankAccountResponse> updateAccountStatus(
            @PathVariable Long accountId,
            @RequestParam String newStatus) {
        // TODO: Implementar actualización de estado (solo admin)
        return ResponseEntity.ok(new BankAccountResponse());
    }
}
