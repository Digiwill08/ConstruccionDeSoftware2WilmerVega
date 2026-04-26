package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.application.usecases.EmployeeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller for TELLER_EMPLOYEE / COMMERCIAL_EMPLOYEE / INTERNAL_ANALYST roles.
 * Full CRUD for bank accounts, clients and loans.
 */
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;

    // ── Bank Accounts ─────────────────────────────────────────────────────────

    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        return ResponseEntity.ok(employeeUseCase.findAllBankAccounts());
    }

    @GetMapping("/bank-accounts/{id}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeUseCase.findBankAccountById(id));
    }

    @GetMapping("/bank-accounts/number/{accountNumber}")
    public ResponseEntity<BankAccount> getBankAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(employeeUseCase.findBankAccountByNumber(accountNumber));
    }

    @PostMapping("/bank-accounts")
    public ResponseEntity<Map<String, Object>> createBankAccount(@RequestBody BankAccount account) {
        BankAccount saved = employeeUseCase.saveBankAccount(account);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Cuenta bancaria creada correctamente");
        response.put("id", saved.getId());
        response.put("accountNumber", saved.getAccountNumber());
        response.put("status", saved.getAccountStatus() != null ? saved.getAccountStatus().name() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/bank-accounts/{id}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long id,
                                                          @RequestBody BankAccount account) {
        account.setId(id);
        return ResponseEntity.ok(employeeUseCase.updateBankAccount(account));
    }

    @DeleteMapping("/bank-accounts/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        employeeUseCase.deleteBankAccountById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Natural Clients ───────────────────────────────────────────────────────

    @GetMapping("/natural-clients")
    public ResponseEntity<List<NaturalClient>> getAllNaturalClients() {
        return ResponseEntity.ok(employeeUseCase.findAllNaturalClients());
    }

    @GetMapping("/natural-clients/{id}")
    public ResponseEntity<NaturalClient> getNaturalClientById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeUseCase.findNaturalClientById(id));
    }

    @PostMapping("/natural-clients")
    public ResponseEntity<Map<String, Object>> createNaturalClient(@RequestBody NaturalClient client) {
        NaturalClient saved = employeeUseCase.saveNaturalClient(client);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Cliente natural creado correctamente");
        response.put("id", saved.getId());
        response.put("documentNumber", saved.getDocumentNumber());
        response.put("role", saved.getRole() != null ? saved.getRole().name() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/natural-clients/{id}")
    public ResponseEntity<NaturalClient> updateNaturalClient(@PathVariable Long id,
                                                              @RequestBody NaturalClient client) {
        client.setId(id);
        return ResponseEntity.ok(employeeUseCase.updateNaturalClient(client));
    }

    @DeleteMapping("/natural-clients/{id}")
    public ResponseEntity<Void> deleteNaturalClient(@PathVariable Long id) {
        employeeUseCase.deleteNaturalClientById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Company Clients ───────────────────────────────────────────────────────

    @GetMapping("/company-clients")
    public ResponseEntity<List<CompanyClient>> getAllCompanyClients() {
        return ResponseEntity.ok(employeeUseCase.findAllCompanyClients());
    }

    @GetMapping("/company-clients/{id}")
    public ResponseEntity<CompanyClient> getCompanyClientById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeUseCase.findCompanyClientById(id));
    }

    @PostMapping("/company-clients")
    public ResponseEntity<Map<String, Object>> createCompanyClient(@RequestBody CompanyClient client) {
        CompanyClient saved = employeeUseCase.saveCompanyClient(client);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Cliente empresa creado correctamente");
        response.put("id", saved.getId());
        response.put("documentNumber", saved.getDocumentNumber());
        response.put("businessName", saved.getBusinessName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/company-clients/{id}")
    public ResponseEntity<CompanyClient> updateCompanyClient(@PathVariable Long id,
                                                              @RequestBody CompanyClient client) {
        client.setId(id);
        return ResponseEntity.ok(employeeUseCase.updateCompanyClient(client));
    }

    @DeleteMapping("/company-clients/{id}")
    public ResponseEntity<Void> deleteCompanyClient(@PathVariable Long id) {
        employeeUseCase.deleteCompanyClientById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(employeeUseCase.findAllLoans());
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeUseCase.findLoanById(id));
    }

    @PostMapping("/loans")
    public ResponseEntity<Map<String, Object>> createLoan(@RequestBody Loan loan) {
        Loan saved = employeeUseCase.saveLoan(loan);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Prestamo creado correctamente");
        response.put("id", saved.getLoanId());
        response.put("status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/loans/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        loan.setLoanId(id);
        return ResponseEntity.ok(employeeUseCase.updateLoan(loan));
    }

    @DeleteMapping("/loans/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        employeeUseCase.deleteLoanById(id);
        return ResponseEntity.noContent().build();
    }
}
