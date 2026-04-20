package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.usecases.ClientUseCase;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for NATURAL_CLIENT / COMPANY_CLIENT / COMPANY_EMPLOYEE roles.
 * Endpoints: /api/client/**
 */
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    // ── Bank Accounts ─────────────────────────────────────────────────────────

    @GetMapping("/bank-accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getBankAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(clientUseCase.findMyBankAccount(accountNumber));
    }

    // ── Transfers ─────────────────────────────────────────────────────────────

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(clientUseCase.findAllTransfers());
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(clientUseCase.findTransferById(id));
    }

    @PostMapping("/transfers")
    public ResponseEntity<Transfer> executeTransfer(@RequestBody Transfer transfer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientUseCase.executeTransfer(transfer));
    }
}
