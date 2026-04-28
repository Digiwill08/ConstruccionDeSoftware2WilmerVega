package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.usecases.ClientUseCase;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    // --- Bank Accounts ---
    @GetMapping("/bank-accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getBankAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(clientUseCase.findMyBankAccount(accountNumber));
    }

    // --- Transfers ---
    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(clientUseCase.findAllTransfers());
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(clientUseCase.findTransferById(id));
    }

    @PostMapping("/transfers")
    public ResponseEntity<Map<String, Object>> executeTransfer(@RequestBody Transfer transfer) {
        try {
            Transfer saved = clientUseCase.executeTransfer(transfer);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Transferencia ejecutada correctamente");
            response.put("id", saved.getTransferId());
            response.put("status", saved.getTransferStatus() != null ? saved.getTransferStatus().name() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
