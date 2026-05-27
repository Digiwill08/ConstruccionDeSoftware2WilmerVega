package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.services.*;

import java.util.List;

public class EmployeeUseCase {

    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;
    private final DeleteBankAccount deleteBankAccount;
    private final UpdateBankAccount updateBankAccount;

    private final CreateNaturalClient createNaturalClient;
    private final FindNaturalClient findNaturalClient;
    private final DeleteNaturalClient deleteNaturalClient;
    private final UpdateNaturalClient updateNaturalClient;

    private final CreateCompanyClient createCompanyClient;
    private final FindCompanyClient findCompanyClient;
    private final DeleteCompanyClient deleteCompanyClient;
    private final UpdateCompanyClient updateCompanyClient;

    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final DeleteLoan deleteLoan;
    private final UpdateLoan updateLoan;

    public EmployeeUseCase(CreateBankAccount createBankAccount, FindBankAccount findBankAccount,
                           DeleteBankAccount deleteBankAccount, UpdateBankAccount updateBankAccount,
                           CreateNaturalClient createNaturalClient, FindNaturalClient findNaturalClient,
                           DeleteNaturalClient deleteNaturalClient, UpdateNaturalClient updateNaturalClient,
                           CreateCompanyClient createCompanyClient, FindCompanyClient findCompanyClient,
                           DeleteCompanyClient deleteCompanyClient, UpdateCompanyClient updateCompanyClient,
                           CreateLoan createLoan, FindLoan findLoan,
                           DeleteLoan deleteLoan, UpdateLoan updateLoan) {
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
        this.deleteBankAccount = deleteBankAccount;
        this.updateBankAccount = updateBankAccount;

        this.createNaturalClient = createNaturalClient;
        this.findNaturalClient = findNaturalClient;
        this.deleteNaturalClient = deleteNaturalClient;
        this.updateNaturalClient = updateNaturalClient;

        this.createCompanyClient = createCompanyClient;
        this.findCompanyClient = findCompanyClient;
        this.deleteCompanyClient = deleteCompanyClient;
        this.updateCompanyClient = updateCompanyClient;

        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.deleteLoan = deleteLoan;
        this.updateLoan = updateLoan;
    }

    // ── BankAccount ───────────────────────────────────────────────────────────
    public List<BankAccount> findAllBankAccounts()              { return findBankAccount.findAll(); }
    public BankAccount findBankAccountById(Long id)             { return findBankAccount.findById(id); }
    public BankAccount findBankAccountByNumber(String number)   { return findBankAccount.findByAccountNumber(number); }
    public BankAccount saveBankAccount(BankAccount account)     { return createBankAccount.save(account); }
    public BankAccount updateBankAccount(BankAccount account)   { return updateBankAccount.update(account); }
    public void deleteBankAccountById(Long id)                  { deleteBankAccount.deleteById(id); }

    // ── NaturalClient ─────────────────────────────────────────────────────────
    public List<NaturalClient> findAllNaturalClients()          { return findNaturalClient.findAll(); }
    public NaturalClient findNaturalClientById(Long id)         { return findNaturalClient.findById(id); }
    public NaturalClient saveNaturalClient(NaturalClient c)     { return createNaturalClient.save(c); }
    public NaturalClient updateNaturalClient(NaturalClient c)   { return updateNaturalClient.update(c); }
    public void deleteNaturalClientById(Long id)                { deleteNaturalClient.deleteById(id); }

    // ── CompanyClient ─────────────────────────────────────────────────────────
    public List<CompanyClient> findAllCompanyClients()          { return findCompanyClient.findAll(); }
    public CompanyClient findCompanyClientById(Long id)         { return findCompanyClient.findById(id); }
    public CompanyClient saveCompanyClient(CompanyClient c)     { return createCompanyClient.save(c); }
    public CompanyClient updateCompanyClient(CompanyClient c)   { return updateCompanyClient.update(c); }
    public void deleteCompanyClientById(Long id)                { deleteCompanyClient.deleteById(id); }

    // ── Loan ──────────────────────────────────────────────────────────────────
    public List<Loan> findAllLoans()                            { return findLoan.findAll(); }
    public Loan findLoanById(Long id)                           { return findLoan.findById(id); }
    public Loan saveLoan(Loan loan)                             { return createLoan.save(loan); }
    public Loan updateLoan(Loan loan)                           { return updateLoan.update(loan); }
    public void deleteLoanById(Long id)                         { deleteLoan.deleteById(id); }
}
