package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.application.usecases.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    // --- Bank Accounts ---
    @GetMapping("/bank-accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getBankAccountByNumber(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(clientUseCase.findMyBankAccount(accountNumber));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Transfers ---
    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(clientUseCase.findAllTransfers());
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clientUseCase.findTransferById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transfers")
    public ResponseEntity<Transfer> executeTransfer(@RequestBody Transfer transfer) {
        try {
            return ResponseEntity.ok(clientUseCase.executeTransfer(transfer));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
