package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.InvalidApprovedAmountException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidDisbursementBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.LoanNotDisbursableException;
import gestiondeunbanco.wilmervega.domain.exceptions.OwnershipViolationException;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.LoanPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisburseLoanServiceTest {

    @Mock
    private LoanPort loanPort;

    @Mock
    private BankAccountPort bankAccountPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private DisburseLoanService service;

    @BeforeEach
    void setUp() {
        service = new DisburseLoanService(loanPort, bankAccountPort, auditLogMongoPort);
    }

    @Test
    void disburseShouldThrowWhenLoanStatusIsNotApproved() {
        Loan loan = buildLoan(1L, 100L, LoanStatus.REJECTED, new BigDecimal("500.00"));
        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(LoanNotDisbursableException.class,
                () -> service.disburse(1L, 10L, 900L, "INTERNAL_ANALYST"));

        verify(bankAccountPort, never()).findById(any());
        verify(auditLogMongoPort, never()).save(any());
    }

    @Test
    void disburseShouldThrowWhenApprovedAmountIsInvalid() {
        Loan loan = buildLoan(1L, 100L, LoanStatus.APPROVED, BigDecimal.ZERO);
        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(InvalidApprovedAmountException.class,
                () -> service.disburse(1L, 10L, 900L, "INTERNAL_ANALYST"));

        verify(bankAccountPort, never()).findById(any());
        verify(auditLogMongoPort, never()).save(any());
    }

    @Test
    void disburseShouldThrowWhenOwnershipCannotBeValidated() {
        Loan loan = buildLoan(1L, null, LoanStatus.APPROVED, new BigDecimal("500.00"));
        BankAccount account = buildAccount(10L, 100L, AccountStatus.ACTIVE, new BigDecimal("1000.00"));

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));
        when(bankAccountPort.findById(10L)).thenReturn(Optional.of(account));

        assertThrows(OwnershipViolationException.class,
                () -> service.disburse(1L, 10L, 900L, "INTERNAL_ANALYST"));

        verify(bankAccountPort, never()).save(any());
        verify(auditLogMongoPort, never()).save(any());
    }

    @Test
    void disburseShouldThrowWhenAccountBalanceIsNull() {
        Loan loan = buildLoan(1L, 100L, LoanStatus.APPROVED, new BigDecimal("500.00"));
        BankAccount account = buildAccount(10L, 100L, AccountStatus.ACTIVE, null);

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));
        when(bankAccountPort.findById(10L)).thenReturn(Optional.of(account));

        assertThrows(InvalidDisbursementBalanceException.class,
                () -> service.disburse(1L, 10L, 900L, "INTERNAL_ANALYST"));

        verify(bankAccountPort, never()).save(any());
        verify(auditLogMongoPort, never()).save(any());
    }

    @Test
    void disburseShouldCreditAccountUpdateLoanAndSaveAuditLog() {
        Loan loan = buildLoan(1L, 100L, LoanStatus.APPROVED, new BigDecimal("500.00"));
        BankAccount account = buildAccount(10L, 100L, AccountStatus.ACTIVE, new BigDecimal("1000.00"));

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));
        when(bankAccountPort.findById(10L)).thenReturn(Optional.of(account));
        when(loanPort.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan result = service.disburse(1L, 10L, 900L, "INTERNAL_ANALYST");

        assertEquals(new BigDecimal("1500.00"), account.getCurrentBalance());
        assertEquals(LoanStatus.DISBURSED, result.getLoanStatus());
        assertNotNull(result.getDisbursementDate());
        assertEquals(account, result.getDisbursementAccount());

        verify(bankAccountPort).save(account);
        verify(loanPort).save(loan);

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        AuditLog savedLog = logCaptor.getValue();

        assertNotNull(savedLog.getOperationDateTime());
        assertNotNull(savedLog.getDetails());
        assertNotNull(savedLog.getDetails().get("fechaHoraOperacion"));
        assertNotNull(savedLog.getDetails().get("fechaDesembolso"));
    }

    private Loan buildLoan(Long loanId, Long applicantId, LoanStatus status, BigDecimal approvedAmount) {
        NaturalClient client = new NaturalClient();
        client.setId(applicantId);

        Loan loan = new Loan();
        loan.setLoanId(loanId);
        loan.setClientApplicant(client);
        loan.setLoanStatus(status);
        loan.setApprovedAmount(approvedAmount);
        return loan;
    }

    private BankAccount buildAccount(Long accountId, Long holderId, AccountStatus status, BigDecimal balance) {
        NaturalClient holder = new NaturalClient();
        holder.setId(holderId);

        BankAccount account = new BankAccount();
        account.setId(accountId);
        account.setHolder(holder);
        account.setAccountNumber("ACC-001");
        account.setAccountStatus(status);
        account.setCurrentBalance(balance);
        return account;
    }
}
