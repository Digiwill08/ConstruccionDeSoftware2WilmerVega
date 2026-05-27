package gestiondeunbanco.wilmervega.domain.services;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpireTransferServiceTest {

    @Mock
    private TransferPort transferPort;

    @Mock
    private AuditLogMongoPort auditLogMongoPort;

    private ExpireTransferService service;

    @BeforeEach
    void setUp() {
        service = new ExpireTransferService(transferPort, auditLogMongoPort);
    }

    @Test
    void expireAllShouldExpireTransfersAndRegisterAudit() {
        Transfer transfer = new Transfer();
        transfer.setTransferId(11L);
        transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
        transfer.setCreationDateTime(LocalDateTime.now().minusHours(2));
        transfer.setAmount(new BigDecimal("200.00"));
        transfer.setCreatorUserId(99L);

        when(transferPort.findAwaitingApprovalOlderThan(any(LocalDateTime.class)))
                .thenReturn(List.of(transfer));

        service.expireAll();

        assertEquals(TransferStatus.EXPIRED, transfer.getTransferStatus());
        verify(transferPort).save(transfer);

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogMongoPort).save(logCaptor.capture());
        assertNotNull(logCaptor.getValue().getOperationDateTime());
        assertEquals("EXPIRED", logCaptor.getValue().getDetails().get("newStatus"));
    }
}
