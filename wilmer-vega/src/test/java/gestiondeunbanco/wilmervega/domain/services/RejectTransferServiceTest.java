package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.TransferNotApprovableException;
import gestiondeunbanco.wilmervega.domain.exceptions.UnauthorizedAccessException;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectTransferServiceTest {

    @Mock
    private TransferPort transferPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private RejectTransferService service;

    @BeforeEach
    void setUp() {
        service = new RejectTransferService(transferPort, auditLogMongoPort);
    }

    @Test
    void rejectShouldThrowWhenRoleIsNotSupervisor() {
        assertThrows(UnauthorizedAccessException.class,
                () -> service.reject(1L, 10L, "CLIENT", "no autorizado"));

        verify(auditLogMongoPort, atLeastOnce()).save(any(AuditLog.class));
    }

    @Test
    void rejectShouldThrowWhenTransferStatusIsInvalid() {
        Transfer transfer = new Transfer();
        transfer.setTransferId(1L);
        transfer.setTransferStatus(TransferStatus.EXECUTED);

        when(transferPort.findById(1L)).thenReturn(Optional.of(transfer));

        assertThrows(TransferNotApprovableException.class,
                () -> service.reject(1L, 10L, "COMPANY_SUPERVISOR", "fuera de flujo"));

        verify(auditLogMongoPort, atLeastOnce()).save(any(AuditLog.class));
    }

    @Test
    void rejectShouldSetRejectedAndLog() {
        Transfer transfer = new Transfer();
        transfer.setTransferId(1L);
        transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
        transfer.setCreationDateTime(LocalDateTime.now().minusMinutes(5));

        when(transferPort.findById(1L)).thenReturn(Optional.of(transfer));
        when(transferPort.save(any(Transfer.class))).thenAnswer(i -> i.getArgument(0));

        Transfer saved = service.reject(1L, 10L, "COMPANY_SUPERVISOR", "riesgo");

        assertEquals(TransferStatus.REJECTED, saved.getTransferStatus());
        assertNotNull(saved.getApprovalDateTime());

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort, atLeastOnce()).save(logCaptor.capture());
        assertNotNull(logCaptor.getAllValues().get(0).getOperationDateTime());
    }
}
