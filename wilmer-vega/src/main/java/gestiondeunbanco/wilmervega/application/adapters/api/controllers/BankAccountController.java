package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountResponse;
import gestiondeunbanco.wilmervega.application.usecases.BankAccountUseCase;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AccountType;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Currency;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountUseCase bankAccountUseCase;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBankAccount(@RequestBody BankAccountRequest request) {
        try {
            BankAccount created = bankAccountUseCase.create(toModel(request));
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Cuenta creada correctamente");
            response.put("id", created.getId());
            response.put("accountNumber", created.getAccountNumber());
            response.put("status", created.getAccountStatus() != null ? created.getAccountStatus().name() : null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllMyAccounts(@RequestParam(required = false) Long holderId) {
        List<BankAccount> accounts = holderId == null ? bankAccountUseCase.findAll() : bankAccountUseCase.findByHolderId(holderId);
        return ResponseEntity.ok(accounts.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(toResponse(bankAccountUseCase.findByAccountNumber(accountNumber)));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable Long accountId) {
        BankAccount account = bankAccountUseCase.findById(accountId);
        return ResponseEntity.ok(account.getCurrentBalance());
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable Long accountId,
            @RequestParam String newStatus) {
        try {
            AccountStatus status = AccountStatus.valueOf(newStatus.trim().toUpperCase(Locale.ROOT));
            BankAccount updated = bankAccountUseCase.updateStatus(accountId, status);
            return ResponseEntity.ok(toResponse(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
    }

    private BankAccount toModel(BankAccountRequest request) {
        BankAccount model = new BankAccount();
        model.setAccountNumber(request.getAccountNumber());
        model.setAccountType(AccountType.valueOf(request.getAccountType().trim().toUpperCase(Locale.ROOT)));
        model.setCurrency(Currency.valueOf(request.getCurrency().trim().toUpperCase(Locale.ROOT)));
        model.setCurrentBalance(request.getInitialBalance());
        model.setAccountStatus(AccountStatus.ACTIVE);
        model.setOpeningDate(LocalDate.now());

        NaturalClient holder = new NaturalClient();
        holder.setId(request.getHolderId());
        model.setHolder(holder);
        return model;
    }

    private BankAccountResponse toResponse(BankAccount model) {
        return new BankAccountResponse(
                model.getId(),
                model.getAccountNumber(),
                model.getAccountType() != null ? model.getAccountType().name() : null,
                model.getCurrency() != null ? model.getCurrency().name() : null,
                model.getCurrentBalance(),
                model.getAccountStatus() != null ? model.getAccountStatus().name() : null,
                model.getOpeningDate() != null ? model.getOpeningDate().toString() : null
        );
    }
}
