package gestiondeunbanco.wilmervega.config;

import gestiondeunbanco.wilmervega.config.security.JwtService;
import gestiondeunbanco.wilmervega.application.usecases.*;
import gestiondeunbanco.wilmervega.domain.ports.*;
import gestiondeunbanco.wilmervega.domain.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring configuration class that registers all domain services and use cases as beans.
 * This follows Hexagonal Architecture: domain is pure Java, Spring wiring is in the config layer.
 */
@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ─── Domain Services ────────────────────────────────────────────────────

    @Bean public CreateBankAccount createBankAccount(BankAccountPort bankAccountPort, NaturalClientPort naturalClientPort, CompanyClientPort companyClientPort) {
        return new CreateBankAccount(bankAccountPort, naturalClientPort, companyClientPort);
    }
    @Bean public FindBankAccount findBankAccount(BankAccountPort p) { return new FindBankAccount(p); }
    @Bean public DeleteBankAccount deleteBankAccount(BankAccountPort p) { return new DeleteBankAccount(p); }
    @Bean public UpdateBankAccount updateBankAccount(BankAccountPort p) { return new UpdateBankAccount(p); }

    @Bean public CreateNaturalClient createNaturalClient(NaturalClientPort naturalClientPort, CompanyClientPort companyClientPort) { return new CreateNaturalClient(naturalClientPort, companyClientPort); }
    @Bean public FindNaturalClient findNaturalClient(NaturalClientPort p) { return new FindNaturalClient(p); }
    @Bean public DeleteNaturalClient deleteNaturalClient(NaturalClientPort p) { return new DeleteNaturalClient(p); }
    @Bean public UpdateNaturalClient updateNaturalClient(NaturalClientPort p) { return new UpdateNaturalClient(p); }

    @Bean public CreateCompanyClient createCompanyClient(CompanyClientPort companyClientPort, NaturalClientPort naturalClientPort) { return new CreateCompanyClient(companyClientPort, naturalClientPort); }
    @Bean public FindCompanyClient findCompanyClient(CompanyClientPort p) { return new FindCompanyClient(p); }
    @Bean public DeleteCompanyClient deleteCompanyClient(CompanyClientPort p) { return new DeleteCompanyClient(p); }
    @Bean public UpdateCompanyClient updateCompanyClient(CompanyClientPort p) { return new UpdateCompanyClient(p); }

    @Bean public CreateLoan createLoan(LoanPort p) { return new CreateLoan(p); }
    @Bean public FindLoan findLoan(LoanPort p) { return new FindLoan(p); }
    @Bean public DeleteLoan deleteLoan(LoanPort p) { return new DeleteLoan(p); }
    @Bean public UpdateLoan updateLoan(LoanPort p) { return new UpdateLoan(p); }

    @Bean public CreateTransfer createTransfer(TransferPort tp, BankAccountPort bp, AuditLogMongoPort ap) {
        return new CreateTransfer(tp, bp, ap);
    }
    @Bean public FindTransfer findTransfer(TransferPort p) { return new FindTransfer(p); }
    @Bean public DeleteTransfer deleteTransfer(TransferPort p) { return new DeleteTransfer(p); }

    @Bean public CreateUser createUser(UserPort p) { return new CreateUser(p); }
    @Bean public FindUser findUser(UserPort p) { return new FindUser(p); }
    @Bean public DeleteUser deleteUser(UserPort p) { return new DeleteUser(p); }
    @Bean public UpdateUser updateUser(UserPort p) { return new UpdateUser(p); }

    @Bean public CreateAuditLog createAuditLog(AuditLogPort p) { return new CreateAuditLog(p); }
    @Bean public FindAuditLog findAuditLog(AuditLogPort p) { return new FindAuditLog(p); }

    // ─── Business Services ───────────────────────────────────────────────────

    @Bean public ApproveLoanService approveLoanService(LoanPort lp, AuditLogMongoPort ap) {
        return new ApproveLoanService(lp, ap);
    }
    @Bean public RejectLoanService rejectLoanService(LoanPort lp, AuditLogMongoPort ap) {
        return new RejectLoanService(lp, ap);
    }
    @Bean public DisburseLoanService disburseLoanService(LoanPort lp, BankAccountPort bp, AuditLogMongoPort ap) {
        return new DisburseLoanService(lp, bp, ap);
    }
    @Bean public ApproveTransferService approveTransferService(TransferPort tp, BankAccountPort bp, AuditLogMongoPort ap) {
        return new ApproveTransferService(tp, bp, ap);
    }
    @Bean public RejectTransferService rejectTransferService(TransferPort tp, AuditLogMongoPort ap) {
        return new RejectTransferService(tp, ap);
    }
    @Bean public ExpireTransferService expireTransferService(TransferPort tp, AuditLogMongoPort ap) {
        return new ExpireTransferService(tp, ap);
    }

    // ─── Use Cases ───────────────────────────────────────────────────────────

    @Bean
    public AdminUseCase adminUseCase(CreateUser cu, FindUser fu, DeleteUser du, FindAuditLog fa) {
        return new AdminUseCase(cu, fu, du, fa);
    }

    @Bean
    public EmployeeUseCase employeeUseCase(CreateBankAccount cba, FindBankAccount fba,
                                            DeleteBankAccount dba, UpdateBankAccount uba,
                                            CreateNaturalClient cnc, FindNaturalClient fnc,
                                            DeleteNaturalClient dnc, UpdateNaturalClient unc,
                                            CreateCompanyClient ccc, FindCompanyClient fcc,
                                            DeleteCompanyClient dcc, UpdateCompanyClient ucc,
                                            CreateLoan cl, FindLoan fl,
                                            DeleteLoan dl, UpdateLoan ul) {
        return new EmployeeUseCase(cba, fba, dba, uba, cnc, fnc, dnc, unc, ccc, fcc, dcc, ucc, cl, fl, dl, ul);
    }

    @Bean
    public CustomerUseCase customerUseCase(CreateNaturalClient cnc, FindNaturalClient fnc, DeleteNaturalClient dnc, UpdateNaturalClient unc,
                                           CreateCompanyClient ccc, FindCompanyClient fcc, DeleteCompanyClient dcc, UpdateCompanyClient ucc) {
        return new CustomerUseCase(cnc, fnc, dnc, unc, ccc, fcc, dcc, ucc);
    }

    @Bean
    public ClientUseCase clientUseCase(FindBankAccount fba, CreateTransfer ct, FindTransfer ft) {
        return new ClientUseCase(fba, ct, ft);
    }

    @Bean
    public BankAccountUseCase bankAccountUseCase(CreateBankAccount cba, FindBankAccount fba, UpdateBankAccount uba) {
        return new BankAccountUseCase(cba, fba, uba);
    }

    @Bean
    public AnalystUseCase analystUseCase(ApproveLoanService apls, RejectLoanService rls,
                                          DisburseLoanService dls, FindLoan fl, AuditLogMongoPort ap) {
        return new AnalystUseCase(apls, rls, dls, fl, ap);
    }

    @Bean
    public CompanySupervisorUseCase companySupervisorUseCase(ApproveTransferService ats,
                                                              RejectTransferService rts, FindTransfer ft) {
        return new CompanySupervisorUseCase(ats, rts, ft);
    }

    @Bean
    public AuthUseCase authUseCase(UserPort userPort, PasswordEncoder passwordEncoder, JwtService jwtService) {
        return new AuthUseCase(userPort, passwordEncoder, jwtService);
    }
}
