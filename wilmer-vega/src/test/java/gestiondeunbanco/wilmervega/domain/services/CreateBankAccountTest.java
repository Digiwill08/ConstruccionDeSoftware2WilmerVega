package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateAccountNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidBankAccountException;
import gestiondeunbanco.wilmervega.domain.exceptions.MissingAccountHolderException;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AccountType;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.Currency;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.BankAccountPort;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountTest {

    @Mock
    private BankAccountPort bankAccountPort;

    @Mock
    private NaturalClientPort naturalClientPort;

    @Mock
    private CompanyClientPort companyClientPort;

    private CreateBankAccount createBankAccount;

    @BeforeEach
    void setUp() {
        createBankAccount = new CreateBankAccount(bankAccountPort, naturalClientPort, companyClientPort);
    }

    @Test
    void saveShouldThrowWhenBankAccountIsNull() {
        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(null);
        });
    }

    @Test
    void saveShouldThrowWhenAccountNumberIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber(null);

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenAccountTypeIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(null);

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenCurrencyIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(null);

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenAccountStatusIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(null);

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenOpeningDateIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(null);

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenCurrentBalanceIsNegative() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDate.now());
        account.setCurrentBalance(new BigDecimal("-100"));

        assertThrows(InvalidBankAccountException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenHolderIsNull() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDate.now());
        account.setCurrentBalance(BigDecimal.ZERO);
        account.setHolder(null);

        assertThrows(MissingAccountHolderException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenHolderIdNotFound() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDate.now());
        account.setCurrentBalance(BigDecimal.ZERO);

        NaturalClient holder = new NaturalClient();
        holder.setId(999L);
        account.setHolder(holder);

        when(naturalClientPort.findById(999L)).thenReturn(Optional.empty());
        when(companyClientPort.findById(999L)).thenReturn(Optional.empty());

        assertThrows(MissingAccountHolderException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldThrowWhenAccountNumberAlreadyExists() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDate.now());
        account.setCurrentBalance(BigDecimal.ZERO);

        NaturalClient holder = new NaturalClient();
        holder.setId(1L);
        account.setHolder(holder);

        when(naturalClientPort.findById(1L)).thenReturn(Optional.of(holder));
        when(bankAccountPort.existsByAccountNumber("1234567890")).thenReturn(true);

        assertThrows(DuplicateAccountNumberException.class, () -> {
            createBankAccount.save(account);
        });
    }

    @Test
    void saveShouldSuccessfullyCreateValidBankAccount() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.COP);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDate.now());
        account.setCurrentBalance(new BigDecimal("1000"));

        NaturalClient holder = new NaturalClient();
        holder.setId(1L);
        account.setHolder(holder);

        when(naturalClientPort.findById(1L)).thenReturn(Optional.of(holder));
        when(bankAccountPort.existsByAccountNumber("1234567890")).thenReturn(false);
        when(bankAccountPort.save(any(BankAccount.class))).thenReturn(account);

        BankAccount result = createBankAccount.save(account);
        assert result != null;
        assert result.getAccountNumber().equals("1234567890");
    }
}
