package gestiondeunbanco.wilmervega.config;

import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserPort userPort;
    private final NaturalClientPort naturalClientPort;
    private final BankAccountPort bankAccountPort;
    private final LoanPort loanPort;

    @Override
    public void run(String... args) {
        log.info("Populating H2 database with test data...");

        migrateLegacyAdministratorRole();

        // 1. Create Internal Analyst (Admin/Analyst role) if missing
        if (!userPort.existsByUsername("analista")) {
            User analyst = new User();
            analyst.setFullName("Analista Interno");
            analyst.setUsername("analista");
            analyst.setPassword("pass123");
            analyst.setSystemRole(SystemRole.INTERNAL_ANALYST);
            analyst.setUserStatus(UserStatus.ACTIVE);
            userPort.save(analyst);
            log.info("Created Analyst user: analista / pass123");
        }

        // 2. Create Company Supervisor if missing
        if (!userPort.existsByUsername("supervisor")) {
            User supervisor = new User();
            supervisor.setFullName("Supervisor Empresa");
            supervisor.setUsername("supervisor");
            supervisor.setPassword("pass123");
            supervisor.setSystemRole(SystemRole.COMPANY_SUPERVISOR);
            supervisor.setUserStatus(UserStatus.ACTIVE);
            userPort.save(supervisor);
            log.info("Created Supervisor user: supervisor / pass123");
        }

        // 3. Reuse existing test client or create one
        NaturalClient client = naturalClientPort.findByDocumentNumber("12345678")
                .orElseGet(() -> {
                    NaturalClient c = new NaturalClient();
                    c.setDocumentNumber("12345678");
                    c.setFullName("Wilmer Vega");
                    c.setEmail("wilmer@example.com");
                    c.setPhone("3001234567");
                    c.setAddress("Calle 123");
                    c.setBirthDate(LocalDate.of(1990, 1, 1));
                    return naturalClientPort.save(c);
                });

        // 4. Create Account for the client if missing
        if (!bankAccountPort.existsByAccountNumber("SV-001")) {
            BankAccount account = new BankAccount();
            account.setAccountNumber("SV-001");
            account.setAccountType(AccountType.SAVINGS);
            account.setCurrentBalance(new BigDecimal("15000000")); // Enough for high value transfers
            account.setCurrency(Currency.COP);
            account.setAccountStatus(AccountStatus.ACTIVE);
            account.setOpeningDate(LocalDate.now());
            account.setHolder(client);
            bankAccountPort.save(account);
            log.info("Created Client Wilmer with account SV-001 (Balance: 15M COP)");
        }

        // 5. Create a Loan awaiting review only if no loans exist for this client
        if (loanPort.findByClientDocument("12345678").isEmpty()) {
            Loan loan = new Loan();
            loan.setLoanType(LoanType.CONSUMER);
            loan.setRequestedAmount(new BigDecimal("20000000"));
            loan.setInterestRate(new BigDecimal("1.5"));
            loan.setTermInMonths(36);
            loan.setLoanStatus(LoanStatus.UNDER_REVIEW);
            loan.setClientApplicant(client);
            loanPort.save(loan);
            log.info("Created Loan UNDER_REVIEW for $20M COP");
        }

        log.info("Data initialization complete. System ready for testing.");
    }

    private void migrateLegacyAdministratorRole() {
        userPort.findAll().stream()
                .filter(user -> user.getSystemRole() != null)
                .filter(user -> "ADMINISTRATOR".equalsIgnoreCase(user.getSystemRole().name()))
                .forEach(user -> {
                    user.setSystemRole(SystemRole.INTERNAL_ANALYST);
                    userPort.save(user);
                    log.info("Migrated legacy ADMINISTRATOR user '{}' to INTERNAL_ANALYST.", user.getUsername());
                });
    }
}
