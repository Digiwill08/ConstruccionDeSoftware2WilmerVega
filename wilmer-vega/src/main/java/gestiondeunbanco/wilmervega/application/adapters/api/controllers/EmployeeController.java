package gestiondeunbanco.wilmervega.application.adapters.api.controllers;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.application.usecases.EmployeeUseCase;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller para roles TELLER_EMPLOYEE / COMMERCIAL_EMPLOYEE / INTERNAL_ANALYST.
 * CRUD completo de cuentas bancarias, clientes y prestamos.
 *
 * Segregacion de Funciones (Prompt Maestro - Seccion 3):
 *  - TELLER_EMPLOYEE: acceso a clientes y cuentas (SIN acceso a bitacoras ni datos de riesgo)
 *  - COMMERCIAL_EMPLOYEE: acceso a clientes y cuentas
 *  - INTERNAL_ANALYST: acceso completo incluido gestion de prestamos
 *  - Modificacion de estados de prestamos: SOLO INTERNAL_ANALYST
 */
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TELLER_EMPLOYEE','COMMERCIAL_EMPLOYEE','INTERNAL_ANALYST')")
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
    public ResponseEntity<Map<String, Object>> createBankAccount(@Valid @RequestBody BankAccount account) {
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
                                                          @Valid @RequestBody BankAccount account) {
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
    public ResponseEntity<Map<String, Object>> createNaturalClient(@Valid @RequestBody NaturalClient client) {
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
                                                              @Valid @RequestBody NaturalClient client) {
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
    public ResponseEntity<Map<String, Object>> createCompanyClient(@Valid @RequestBody CompanyClient client) {
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
                                                              @Valid @RequestBody CompanyClient client) {
        client.setId(id);
        return ResponseEntity.ok(employeeUseCase.updateCompanyClient(client));
    }

    @DeleteMapping("/company-clients/{id}")
    public ResponseEntity<Void> deleteCompanyClient(@PathVariable Long id) {
        employeeUseCase.deleteCompanyClientById(id);
        return ResponseEntity.noContent().build();
    }

    // ═ Loans ────────────────────────────────────────────────────────

    /** Consulta de prestamos: todos los roles con acceso a empleados */
    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(employeeUseCase.findAllLoans());
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeUseCase.findLoanById(id));
    }

    /**
     * Creacion de prestamos: TELLER_EMPLOYEE puede iniciar solicitudes.
     * La modificacion de ESTADOS (aprobar/rechazar/desembolsar) es exclusiva del INTERNAL_ANALYST
     * y se realiza a traves del AnalystController (/api/analyst).
     */
    @PostMapping("/loans")
    public ResponseEntity<Map<String, Object>> createLoan(@Valid @RequestBody Loan loan) {
        Loan saved = employeeUseCase.saveLoan(loan);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Prestamo creado correctamente");
        response.put("id", saved.getLoanId());
        response.put("status", saved.getLoanStatus() != null ? saved.getLoanStatus().name() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Modificacion directa de prestamos: SOLO INTERNAL_ANALYST */
    @PutMapping("/loans/{id}")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        loan.setLoanId(id);
        return ResponseEntity.ok(employeeUseCase.updateLoan(loan));
    }

    /** Eliminacion de prestamos: SOLO INTERNAL_ANALYST */
    @DeleteMapping("/loans/{id}")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        employeeUseCase.deleteLoanById(id);
        return ResponseEntity.noContent().build();
    }
}
