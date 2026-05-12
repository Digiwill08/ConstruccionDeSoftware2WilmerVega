package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.InvalidTransferBalanceException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidTransferRequestException;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTransferTest {

    @Mock
    private TransferPort transferPort;

    @Mock
    private BankAccountPort bankAccountPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private CreateTransfer service;

    @BeforeEach
    void setUp() {
        service = new CreateTransfer(transferPort, bankAccountPort, auditLogMongoPort);
    }

    @Test
    void saveShouldThrowWhenTransferIsNull() {
        assertThrows(InvalidTransferRequestException.class, () -> service.save(null));
    }

    @Test
    void saveShouldThrowWhenSourceBalanceIsNull() {
        Transfer transfer = buildTransfer(new BigDecimal("100.00"));
        transfer.setDestinationAccount(null);
        BankAccount source = buildAccount("SRC", AccountStatus.ACTIVE, null);

        when(bankAccountPort.findByAccountNumber("SRC")).thenReturn(Optional.of(source));

        assertThrows(InvalidTransferBalanceException.class, () -> service.save(transfer));
    }

    @Test
    void saveShouldCreateAwaitingApprovalForEnterpriseAmount() {
        Transfer transfer = buildTransfer(new BigDecimal("10000001"));
        transfer.setCreatorUserId(77L);
        transfer.setDestinationAccount(null);

        BankAccount source = buildAccount("SRC", AccountStatus.ACTIVE, new BigDecimal("50000000"));
        when(bankAccountPort.findByAccountNumber("SRC")).thenReturn(Optional.of(source));
        when(transferPort.save(any(Transfer.class))).thenAnswer(i -> i.getArgument(0));

        Transfer saved = service.save(transfer);

        assertEquals(TransferStatus.AWAITING_APPROVAL, saved.getTransferStatus());
        assertNotNull(saved.getCreationDateTime());

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        assertNotNull(logCaptor.getValue().getOperationDateTime());
    }

    @Test
    void saveShouldExecuteForLowAmount() {
        Transfer transfer = buildTransfer(new BigDecimal("100.00"));
        transfer.setCreatorUserId(88L);

        BankAccount source = buildAccount("SRC", AccountStatus.ACTIVE, new BigDecimal("500.00"));
        BankAccount destination = buildAccount("DST", AccountStatus.ACTIVE, new BigDecimal("10.00"));

        when(bankAccountPort.findByAccountNumber("SRC")).thenReturn(Optional.of(source));
        when(bankAccountPort.findByAccountNumber("DST")).thenReturn(Optional.of(destination));
        when(transferPort.save(any(Transfer.class))).thenAnswer(i -> i.getArgument(0));

        Transfer saved = service.save(transfer);

        assertEquals(TransferStatus.EXECUTED, saved.getTransferStatus());
        assertEquals(new BigDecimal("400.00"), source.getCurrentBalance());
        assertEquals(new BigDecimal("110.00"), destination.getCurrentBalance());

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        assertNotNull(logCaptor.getValue().getDetails().get("fechaHoraOperacion"));
    }

    private Transfer buildTransfer(BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAmount(amount);

        BankAccount source = new BankAccount();
        source.setAccountNumber("SRC");
        transfer.setSourceAccount(source);

        BankAccount destination = new BankAccount();
        destination.setAccountNumber("DST");
        transfer.setDestinationAccount(destination);

        return transfer;
    }

    private BankAccount buildAccount(String number, AccountStatus status, BigDecimal balance) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(number);
        account.setAccountStatus(status);
        account.setCurrentBalance(balance);
        return account;
    }
}
