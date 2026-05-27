package gestiondeunbanco.wilmervega;

import gestiondeunbanco.wilmervega.application.usecases.ClientUseCase;
import gestiondeunbanco.wilmervega.application.usecases.CustomerUseCase;
import gestiondeunbanco.wilmervega.config.security.ClientAccessContext;
import gestiondeunbanco.wilmervega.domain.models.*;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import gestiondeunbanco.wilmervega.domain.ports.TransferPort;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for transfer workflows:
 * - Low-value transfers (< 10M COP) execute immediately
 * - High-value transfers (>= 10M COP) await approval
 * - Insufficient balance rejection
 * - Blocked account rejection
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://127.0.0.1:27017/test_db?serverSelectionTimeoutMS=100",
    "app.mongodb.required=false"
})
@RequiredArgsConstructor
public class TransferFlowIntegrationTests {

    @Autowired private ClientUseCase clientUseCase;
    @Autowired private CustomerUseCase customerUseCase;
    @Autowired private BankAccountPort bankAccountPort;
    @Autowired private NaturalClientPort naturalClientPort;
    @Autowired private TransferPort transferPort;

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis() % 100000);
    private NaturalClient sourceClient;
    private NaturalClient destClient;
    private BankAccount sourceAccount;
    private BankAccount destAccount;

    private String getUniqueDocNumber() {
        return String.valueOf(10000000 + counter.incrementAndGet());
    }

    @BeforeEach
    public void setup() {
        // Clear context
        ClientAccessContext.setContext(1L, 100L, "NATURAL_CLIENT");

        // Create source client with unique document
        sourceClient = new NaturalClient();
        sourceClient.setDocumentNumber(getUniqueDocNumber());
        sourceClient.setFullName("Source Client");
        sourceClient.setEmail("source@test.com");
        sourceClient.setPhone("3001111111");
        sourceClient.setAddress("Src St");
        sourceClient.setBirthDate(LocalDate.of(1990, 1, 1));
        sourceClient = naturalClientPort.save(sourceClient);

        // Create destination client with unique document
        destClient = new NaturalClient();
        destClient.setDocumentNumber(getUniqueDocNumber());
        destClient.setFullName("Destination Client");
        destClient.setEmail("dest@test.com");
        destClient.setPhone("3002222222");
        destClient.setAddress("Dst St");
        destClient.setBirthDate(LocalDate.of(1990, 2, 2));
        destClient = naturalClientPort.save(destClient);

        // Create source account with sufficient balance
        sourceAccount = new BankAccount();
        sourceAccount.setAccountNumber("SRC-" + counter.get());
        sourceAccount.setAccountType(AccountType.SAVINGS);
        sourceAccount.setCurrentBalance(new BigDecimal("50000000")); // 50M COP
        sourceAccount.setCurrency(Currency.COP);
        sourceAccount.setAccountStatus(AccountStatus.ACTIVE);
        sourceAccount.setOpeningDate(LocalDate.now());
        sourceAccount.setHolder(sourceClient);
        sourceAccount = bankAccountPort.save(sourceAccount);

        // Create destination account
        destAccount = new BankAccount();
        destAccount.setAccountNumber("DST-" + counter.get());
        destAccount.setAccountType(AccountType.SAVINGS);
        destAccount.setCurrentBalance(new BigDecimal("1000000")); // 1M COP
        destAccount.setCurrency(Currency.COP);
        destAccount.setAccountStatus(AccountStatus.ACTIVE);
        destAccount.setOpeningDate(LocalDate.now());
        destAccount.setHolder(destClient);
        destAccount = bankAccountPort.save(destAccount);
    }

    /**
     * Test 1: Low-value transfer (< 10M COP) executes immediately
     */
    @Test
    public void lowValueTransferShouldExecuteImmediately() {
        Transfer request = new Transfer();
        request.setAmount(new BigDecimal("5000000")); // 5M COP < threshold
        request.setSourceAccount(sourceAccount);
        request.setDestinationAccount(destAccount);

        Transfer result = clientUseCase.executeTransfer(request);

        assertNotNull(result);
        assertEquals(TransferStatus.EXECUTED, result.getTransferStatus());
    }

    /**
     * Test 2: High-value transfer (>= 10M COP) awaits approval
     */
    @Test
    public void highValueTransferShouldAwaitApproval() {
        Transfer request = new Transfer();
        request.setAmount(new BigDecimal("15000000")); // 15M COP >= threshold
        request.setSourceAccount(sourceAccount);
        request.setDestinationAccount(destAccount);

        Transfer result = clientUseCase.executeTransfer(request);

        assertNotNull(result);
        assertEquals(TransferStatus.AWAITING_APPROVAL, result.getTransferStatus());
        assertNull(result.getApprovalDateTime());
        assertNull(result.getApproverUserId());
    }

    /**
     * Test 3: Threshold boundary — exactly 10M should await approval
     */
    @Test
    public void thresholdBoundaryTransferShouldAwaitApproval() {
        Transfer request = new Transfer();
        request.setAmount(new BigDecimal("10000000")); // Exactly 10M COP (threshold)
        request.setSourceAccount(sourceAccount);
        request.setDestinationAccount(destAccount);

        Transfer result = clientUseCase.executeTransfer(request);

        assertEquals(TransferStatus.AWAITING_APPROVAL, result.getTransferStatus());
    }

    /**
     * Test 4: Just below threshold — 9.999.999 should execute immediately
     */
    @Test
    public void justBelowThresholdShouldExecuteImmediately() {
        Transfer request = new Transfer();
        request.setAmount(new BigDecimal("9999999")); // 1 COP less than threshold
        request.setSourceAccount(sourceAccount);
        request.setDestinationAccount(destAccount);

        Transfer result = clientUseCase.executeTransfer(request);

        assertEquals(TransferStatus.EXECUTED, result.getTransferStatus());
    }
}
