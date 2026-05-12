package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.ClientUnderageException;
import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidContactInformationException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidNaturalClientException;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateNaturalClientTest {

    @Mock
    private NaturalClientPort naturalClientPort;

    @Mock
    private CompanyClientPort companyClientPort;

    private CreateNaturalClient createNaturalClient;

    @BeforeEach
    void setUp() {
        createNaturalClient = new CreateNaturalClient(naturalClientPort, companyClientPort);
    }

    @Test
    void saveShouldThrowWhenNaturalClientIsNull() {
        assertThrows(InvalidNaturalClientException.class, () -> {
            createNaturalClient.save(null);
        });
    }

    @Test
    void saveShouldThrowWhenFullNameIsNull() {
        NaturalClient client = new NaturalClient();
        client.setFullName(null);

        assertThrows(InvalidNaturalClientException.class, () -> {
            createNaturalClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenDocumentNumberIsNotNumeric() {
        NaturalClient client = new NaturalClient();
        client.setFullName("Juan Perez");
        client.setDocumentNumber("CC123ABC");

        assertThrows(InvalidDocumentNumberException.class, () -> {
            createNaturalClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenDocumentNumberAlreadyExists() {
        String docNum = "1234567890";
        NaturalClient client = new NaturalClient();
        client.setFullName("Juan Perez");
        client.setDocumentNumber(docNum);

        when(naturalClientPort.existsByDocumentNumber(docNum)).thenReturn(true);

        assertThrows(DuplicateDocumentNumberException.class, () -> {
            createNaturalClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenClientIsUnderage() {
        String docNum = "1234567890";
        NaturalClient client = new NaturalClient();
        client.setFullName("Juan Perez");
        client.setDocumentNumber(docNum);
        client.setBirthDate(LocalDate.now().minusYears(15));

        when(naturalClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(companyClientPort.existsByDocumentNumber(docNum)).thenReturn(false);

        assertThrows(ClientUnderageException.class, () -> {
            createNaturalClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenEmailIsInvalid() {
        String docNum = "1234567890";
        NaturalClient client = new NaturalClient();
        client.setFullName("Juan Perez");
        client.setDocumentNumber(docNum);
        client.setBirthDate(LocalDate.now().minusYears(25));
        client.setEmail("invalid-email");

        when(naturalClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(companyClientPort.existsByDocumentNumber(docNum)).thenReturn(false);

        assertThrows(InvalidContactInformationException.class, () -> {
            createNaturalClient.save(client);
        });
    }

    @Test
    void saveShouldSuccessfullyCreateValidNaturalClient() {
        String docNum = "1234567890";
        NaturalClient client = new NaturalClient();
        client.setFullName("Juan Perez");
        client.setDocumentNumber(docNum);
        client.setBirthDate(LocalDate.now().minusYears(25));
        client.setEmail("juan@example.com");
        client.setPhone("3001234567");
        client.setAddress("Calle 1 #1-1");
        client.setRole(SystemRole.NATURAL_CLIENT);

        when(naturalClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(companyClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(naturalClientPort.save(any(NaturalClient.class))).thenReturn(client);

        NaturalClient result = createNaturalClient.save(client);
        assert result != null;
        assert result.getFullName().equals("Juan Perez");
    }
}
