package gestiondeunbanco.wilmervega.domain.services;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectLoanServiceTest {

    @Mock
    private LoanPort loanPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private RejectLoanService service;

    @BeforeEach
    void setUp() {
        service = new RejectLoanService(loanPort, auditLogMongoPort);
    }

    @Test
    void rejectShouldThrowWhenLoanStatusIsNotUnderReview() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanStatus(LoanStatus.APPROVED);

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(LoanNotReviewableException.class,
                () -> service.reject(1L, 7L, "INTERNAL_ANALYST", "motivo"));
    }

    @Test
    void rejectShouldSetRejectedStateAndLog() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanStatus(LoanStatus.UNDER_REVIEW);

        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));
        when(loanPort.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));

        Loan saved = service.reject(1L, 7L, "INTERNAL_ANALYST", "riesgo alto");

        assertEquals(LoanStatus.REJECTED, saved.getLoanStatus());

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        assertNotNull(logCaptor.getValue().getOperationDateTime());
        assertEquals("riesgo alto", logCaptor.getValue().getDetails().get("motivo"));
    }
}
