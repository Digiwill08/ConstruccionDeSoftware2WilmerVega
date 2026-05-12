package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidCompanyClientException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidContactInformationException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidDocumentNumberException;
import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import gestiondeunbanco.wilmervega.domain.ports.NaturalClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCompanyClientTest {

    @Mock
    private CompanyClientPort companyClientPort;

    @Mock
    private NaturalClientPort naturalClientPort;

    private CreateCompanyClient createCompanyClient;

    @BeforeEach
    void setUp() {
        createCompanyClient = new CreateCompanyClient(companyClientPort, naturalClientPort);
    }

    @Test
    void saveShouldThrowWhenCompanyClientIsNull() {
        assertThrows(InvalidCompanyClientException.class, () -> {
            createCompanyClient.save(null);
        });
    }

    @Test
    void saveShouldThrowWhenBusinessNameIsNull() {
        CompanyClient client = new CompanyClient();
        client.setBusinessName(null);

        assertThrows(InvalidCompanyClientException.class, () -> {
            createCompanyClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenEmailIsInvalid() {
        CompanyClient client = new CompanyClient();
        client.setBusinessName("Empresa LTDA");
        client.setEmail("invalid-email");

        assertThrows(InvalidContactInformationException.class, () -> {
            createCompanyClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenDocumentNumberIsNotNumeric() {
        CompanyClient client = new CompanyClient();
        client.setBusinessName("Empresa LTDA");
        client.setEmail("empresa@example.com");
        client.setPhone("3001234567");
        client.setAddress("Calle 1 #1-1");
        client.setDocumentNumber("NIT-123ABC");

        assertThrows(InvalidDocumentNumberException.class, () -> {
            createCompanyClient.save(client);
        });
    }

    @Test
    void saveShouldThrowWhenDocumentNumberAlreadyExistsInCompanyClients() {
        String docNum = "800123456";
        CompanyClient client = new CompanyClient();
        client.setBusinessName("Empresa LTDA");
        client.setEmail("empresa@example.com");
        client.setPhone("3001234567");
        client.setAddress("Calle 1 #1-1");
        client.setDocumentNumber(docNum);

        when(companyClientPort.existsByDocumentNumber(docNum)).thenReturn(true);

        assertThrows(DuplicateDocumentNumberException.class, () -> {
            createCompanyClient.save(client);
        });
    }

    @Test
    void saveShouldSuccessfullyCreateValidCompanyClient() {
        String docNum = "800123456";
        CompanyClient client = new CompanyClient();
        client.setBusinessName("Empresa LTDA");
        client.setEmail("empresa@example.com");
        client.setPhone("3001234567");
        client.setAddress("Calle 1 #1-1");
        client.setDocumentNumber(docNum);

        NaturalClient representative = new NaturalClient();
        representative.setId(1L);
        client.setLegalRepresentative(representative);

        when(companyClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(naturalClientPort.existsByDocumentNumber(docNum)).thenReturn(false);
        when(naturalClientPort.findById(1L)).thenReturn(Optional.of(representative));
        when(companyClientPort.save(any(CompanyClient.class))).thenReturn(client);

        CompanyClient result = createCompanyClient.save(client);
        assert result != null;
        assert result.getBusinessName().equals("Empresa LTDA");
    }
}
