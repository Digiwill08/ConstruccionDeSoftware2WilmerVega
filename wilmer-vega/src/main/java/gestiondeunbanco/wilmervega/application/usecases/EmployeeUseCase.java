package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.services.*;

import java.util.List;
import java.util.Optional;

public class EmployeeUseCase {

    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;
    private final DeleteBankAccount deleteBankAccount;
    
    private final CreateNaturalClient createNaturalClient;
    private final FindNaturalClient findNaturalClient;
    private final DeleteNaturalClient deleteNaturalClient;
    
    private final CreateCompanyClient createCompanyClient;
    private final FindCompanyClient findCompanyClient;
    private final DeleteCompanyClient deleteCompanyClient;
    
    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final DeleteLoan deleteLoan;

    public EmployeeUseCase(CreateBankAccount createBankAccount, FindBankAccount findBankAccount, DeleteBankAccount deleteBankAccount,
                           CreateNaturalClient createNaturalClient, FindNaturalClient findNaturalClient, DeleteNaturalClient deleteNaturalClient,
                           CreateCompanyClient createCompanyClient, FindCompanyClient findCompanyClient, DeleteCompanyClient deleteCompanyClient,
                           CreateLoan createLoan, FindLoan findLoan, DeleteLoan deleteLoan) {
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
        this.deleteBankAccount = deleteBankAccount;
        
        this.createNaturalClient = createNaturalClient;
        this.findNaturalClient = findNaturalClient;
        this.deleteNaturalClient = deleteNaturalClient;
        
        this.createCompanyClient = createCompanyClient;
        this.findCompanyClient = findCompanyClient;
        this.deleteCompanyClient = deleteCompanyClient;
        
        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.deleteLoan = deleteLoan;
    }

    // --- BankAccount ---
    public List<BankAccount> findAllBankAccounts() { return findBankAccount.findAll(); }
    public Optional<BankAccount> findBankAccountById(Long id) { return findBankAccount.findById(id); }
    public BankAccount saveBankAccount(BankAccount account) { return createBankAccount.save(account); }
    public void deleteBankAccountById(Long id) { deleteBankAccount.deleteById(id); }
    public Optional<BankAccount> findBankAccountByNumber(String number) { return findBankAccount.findByAccountNumber(number); }

    // --- NaturalClient ---
    public List<NaturalClient> findAllNaturalClients() { return findNaturalClient.findAll(); }
    public Optional<NaturalClient> findNaturalClientById(Long id) { return findNaturalClient.findById(id); }
    public NaturalClient saveNaturalClient(NaturalClient client) { return createNaturalClient.save(client); }
    public void deleteNaturalClientById(Long id) { deleteNaturalClient.deleteById(id); }

    // --- CompanyClient ---
    public List<CompanyClient> findAllCompanyClients() { return findCompanyClient.findAll(); }
    public Optional<CompanyClient> findCompanyClientById(Long id) { return findCompanyClient.findById(id); }
    public CompanyClient saveCompanyClient(CompanyClient client) { return createCompanyClient.save(client); }
    public void deleteCompanyClientById(Long id) { deleteCompanyClient.deleteById(id); }

    // --- Loan ---
    public List<Loan> findAllLoans() { return findLoan.findAll(); }
    public Optional<Loan> findLoanById(Long id) { return findLoan.findById(id); }
    public Loan saveLoan(Loan loan) { return createLoan.save(loan); }
    public void deleteLoanById(Long id) { deleteLoan.deleteById(id); }
}
