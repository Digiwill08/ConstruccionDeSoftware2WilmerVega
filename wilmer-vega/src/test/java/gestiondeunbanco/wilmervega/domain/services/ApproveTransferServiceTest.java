package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.AccountBlockedException;
import gestiondeunbanco.wilmervega.domain.exceptions.InsufficientBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.exceptions.TransferNotApprovableException;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveTransferServiceTest {

    @Mock
    private TransferPort transferPort;

    @Mock
    private BankAccountPort bankAccountPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private ApproveTransferService service;

    @BeforeEach
    void setUp() {
        service = new ApproveTransferService(transferPort, bankAccountPort, auditLogMongoPort);
    }

    @Test
    void approveShouldThrowWhenStatusNotAwaiting() {
        Transfer t = buildTransfer(1L, TransferStatus.EXECUTED, LocalDateTime.now().minusMinutes(10), new BigDecimal("100.00"));
        when(transferPort.findById(1L)).thenReturn(Optional.of(t));

        assertThrows(TransferNotApprovableException.class, () -> service.approve(1L, 100L, "COMPANY_SUPERVISOR"));
    }

    @Test
    void approveShouldExpireAndThrowIfPastWindow() {
        Transfer t = buildTransfer(1L, TransferStatus.AWAITING_APPROVAL, LocalDateTime.now().minusHours(2), new BigDecimal("100.00"));
        when(transferPort.findById(1L)).thenReturn(Optional.of(t));

        assertThrows(TransferNotApprovableException.class, () -> service.approve(1L, 100L, "COMPANY_SUPERVISOR"));
        assertEquals(TransferStatus.EXPIRED, t.getTransferStatus());
        verify(transferPort).save(t);
    }

    @Test
    void approveShouldThrowWhenSourceAccountBlocked() {
        Transfer t = buildTransfer(1L, TransferStatus.AWAITING_APPROVAL, LocalDateTime.now(), new BigDecimal("100.00"));
        when(transferPort.findById(1L)).thenReturn(Optional.of(t));

        BankAccount src = new BankAccount();
        src.setAccountNumber("SRC-1");
        src.setAccountStatus(AccountStatus.BLOCKED);

        when(bankAccountPort.findByAccountNumber("SRC-1")).thenReturn(Optional.of(src));

        assertThrows(AccountBlockedException.class, () -> service.approve(1L, 100L, "COMPANY_SUPERVISOR"));
    }

    @Test
    void approveShouldThrowWhenInsufficientBalance() {
        Transfer t = buildTransfer(1L, TransferStatus.AWAITING_APPROVAL, LocalDateTime.now(), new BigDecimal("500.00"));
        when(transferPort.findById(1L)).thenReturn(Optional.of(t));

        BankAccount src = new BankAccount();
        src.setAccountNumber("SRC-1");
        src.setAccountStatus(AccountStatus.ACTIVE);
        src.setCurrentBalance(new BigDecimal("100.00"));

        when(bankAccountPort.findByAccountNumber("SRC-1")).thenReturn(Optional.of(src));

        assertThrows(InsufficientBalanceException.class, () -> service.approve(1L, 100L, "COMPANY_SUPERVISOR"));
    }

    @Test
    void approveShouldExecuteTransferAndLog() {
        Transfer t = buildTransfer(1L, TransferStatus.AWAITING_APPROVAL, LocalDateTime.now(), new BigDecimal("100.00"));
        BankAccount src = new BankAccount();
        src.setAccountNumber("SRC-1");
        src.setAccountStatus(AccountStatus.ACTIVE);
        src.setCurrentBalance(new BigDecimal("200.00"));

        BankAccount dest = new BankAccount();
        dest.setAccountNumber("DST-1");
        dest.setAccountStatus(AccountStatus.ACTIVE);
        dest.setCurrentBalance(new BigDecimal("50.00"));

        when(transferPort.findById(1L)).thenReturn(Optional.of(t));
        when(bankAccountPort.findByAccountNumber("SRC-1")).thenReturn(Optional.of(src));
        when(bankAccountPort.findByAccountNumber("DST-1")).thenReturn(Optional.of(dest));
        when(transferPort.save(any(Transfer.class))).thenAnswer(i -> i.getArgument(0));

        Transfer saved = service.approve(1L, 100L, "COMPANY_SUPERVISOR");

        assertEquals(new BigDecimal("100.00"), src.getCurrentBalance());
        assertEquals(new BigDecimal("150.00"), dest.getCurrentBalance());
        assertEquals(TransferStatus.EXECUTED, saved.getTransferStatus());
        assertNotNull(saved.getApprovalDateTime());

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(captor.capture());
        AuditLog log = captor.getValue();
        assertNotNull(log.getOperationDateTime());
        assertEquals("COMPANY_SUPERVISOR", log.getUserRole());
    }

    private Transfer buildTransfer(Long id, TransferStatus status, LocalDateTime creation, BigDecimal amount) {
        Transfer t = new Transfer();
        t.setTransferId(id);
        t.setTransferStatus(status);
        t.setCreationDateTime(creation);
        t.setAmount(amount);
        BankAccount src = new BankAccount();
        src.setAccountNumber("SRC-1");
        t.setSourceAccount(src);
        BankAccount dst = new BankAccount();
        dst.setAccountNumber("DST-1");
        t.setDestinationAccount(dst);
        return t;
    }
}
