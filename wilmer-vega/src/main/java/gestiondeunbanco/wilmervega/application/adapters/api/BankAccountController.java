package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountRequest;
import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountResponse;
import gestiondeunbanco.wilmervega.application.usecases.BankAccountUseCase;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountUseCase bankAccountUseCase;

    @PostMapping
    public ResponseEntity<?> createBankAccount(@RequestBody BankAccountRequest request) {
        try {
            BankAccount created = bankAccountUseCase.create(toModel(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllMyAccounts(@RequestParam(required = false) Long holderId) {
        List<BankAccount> accounts = holderId == null ? bankAccountUseCase.findAll() : bankAccountUseCase.findByHolderId(holderId);
        return ResponseEntity.ok(accounts.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(toResponse(bankAccountUseCase.findByAccountNumber(accountNumber)));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable Long accountId) {
        try {
            BankAccount account = bankAccountUseCase.findById(accountId);
            return ResponseEntity.ok(account.getCurrentBalance());
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
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
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
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
