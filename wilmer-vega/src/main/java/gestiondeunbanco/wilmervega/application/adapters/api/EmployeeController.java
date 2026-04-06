package gestiondeunbanco.wilmervega.application.adapters.api;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.application.usecases.EmployeeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;

    // --- Bank Accounts ---
    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        return ResponseEntity.ok(employeeUseCase.findAllBankAccounts());
    }
    
    @PostMapping("/bank-accounts")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccount account) {
        try {
            return ResponseEntity.ok(employeeUseCase.saveBankAccount(account));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Natural Clients ---
    @GetMapping("/natural-clients")
    public ResponseEntity<List<NaturalClient>> getAllNaturalClients() {
        return ResponseEntity.ok(employeeUseCase.findAllNaturalClients());
    }
    
    @PostMapping("/natural-clients")
    public ResponseEntity<NaturalClient> createNaturalClient(@RequestBody NaturalClient client) {
        try {
            return ResponseEntity.ok(employeeUseCase.saveNaturalClient(client));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Company Clients ---
    @GetMapping("/company-clients")
    public ResponseEntity<List<CompanyClient>> getAllCompanyClients() {
        return ResponseEntity.ok(employeeUseCase.findAllCompanyClients());
    }
    
    @PostMapping("/company-clients")
    public ResponseEntity<CompanyClient> createCompanyClient(@RequestBody CompanyClient client) {
        try {
            return ResponseEntity.ok(employeeUseCase.saveCompanyClient(client));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // --- Loans ---
    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(employeeUseCase.findAllLoans());
    }
    
    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        return ResponseEntity.ok(employeeUseCase.saveLoan(loan));
    }
}
