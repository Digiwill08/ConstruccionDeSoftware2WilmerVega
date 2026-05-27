package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.InvalidApprovedAmountException;
import gestiondeunbanco.wilmervega.domain.exceptions.LoanNotReviewableException;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Loan;
import gestiondeunbanco.wilmervega.domain.models.LoanStatus;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApproveLoanServiceTest {

    @Mock
    private LoanPort loanPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private ApproveLoanService service;

    @BeforeEach
    void setUp() {
        service = new ApproveLoanService(loanPort, auditLogMongoPort);
    }

    @Test
    void approveShouldThrowWhenLoanStatusIsNotUnderReview() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanStatus(LoanStatus.APPROVED);

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(LoanNotReviewableException.class,
                () -> service.approve(1L, 5L, "INTERNAL_ANALYST"));
    }

    @Test
    void approveShouldThrowWhenApprovedAmountInvalid() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanStatus(LoanStatus.UNDER_REVIEW);
        loan.setApprovedAmount(BigDecimal.ZERO);

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(InvalidApprovedAmountException.class,
                () -> service.approve(1L, 5L, "INTERNAL_ANALYST"));
    }

    @Test
    void approveShouldSetApprovedStateAndLog() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanStatus(LoanStatus.UNDER_REVIEW);
        loan.setApprovedAmount(new BigDecimal("1000.00"));

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));
        when(loanPort.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));

        Loan saved = service.approve(1L, 5L, "INTERNAL_ANALYST");

        assertEquals(LoanStatus.APPROVED, saved.getLoanStatus());
        assertNotNull(saved.getApprovalDate());
        assertEquals(5L, saved.getApprovedByUserId());

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        assertNotNull(logCaptor.getValue().getOperationDateTime());
    }
}
