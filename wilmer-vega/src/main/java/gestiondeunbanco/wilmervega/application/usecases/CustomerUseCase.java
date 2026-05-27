package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.services.*;

import java.util.List;

public class CustomerUseCase {

    private final CreateNaturalClient createNaturalClient;
    private final FindNaturalClient findNaturalClient;
    private final DeleteNaturalClient deleteNaturalClient;
    private final UpdateNaturalClient updateNaturalClient;

    private final CreateCompanyClient createCompanyClient;
    private final FindCompanyClient findCompanyClient;
    private final DeleteCompanyClient deleteCompanyClient;
    private final UpdateCompanyClient updateCompanyClient;

    public CustomerUseCase(CreateNaturalClient createNaturalClient,
                           FindNaturalClient findNaturalClient,
                           DeleteNaturalClient deleteNaturalClient,
                           UpdateNaturalClient updateNaturalClient,
                           CreateCompanyClient createCompanyClient,
                           FindCompanyClient findCompanyClient,
                           DeleteCompanyClient deleteCompanyClient,
                           UpdateCompanyClient updateCompanyClient) {
        this.createNaturalClient = createNaturalClient;
        this.findNaturalClient = findNaturalClient;
        this.deleteNaturalClient = deleteNaturalClient;
        this.updateNaturalClient = updateNaturalClient;
        this.createCompanyClient = createCompanyClient;
        this.findCompanyClient = findCompanyClient;
        this.deleteCompanyClient = deleteCompanyClient;
        this.updateCompanyClient = updateCompanyClient;
    }

    // --- Natural clients ---
    public List<NaturalClient> findAllNaturalClients() { return findNaturalClient.findAll(); }
    public NaturalClient findNaturalClientById(Long id) { return findNaturalClient.findById(id); }
    public NaturalClient findNaturalClientByDocumentNumber(String documentNumber) { return findNaturalClient.findByDocumentNumber(documentNumber); }
    public NaturalClient saveNaturalClient(NaturalClient client) { return createNaturalClient.save(client); }
    public NaturalClient updateNaturalClient(NaturalClient client) { return updateNaturalClient.update(client); }
    public void deleteNaturalClientById(Long id) { deleteNaturalClient.deleteById(id); }

    // --- Company clients ---
    public List<CompanyClient> findAllCompanyClients() { return findCompanyClient.findAll(); }
    public CompanyClient findCompanyClientById(Long id) { return findCompanyClient.findById(id); }
    public CompanyClient findCompanyClientByDocumentNumber(String documentNumber) { return findCompanyClient.findByDocumentNumber(documentNumber); }
    public CompanyClient saveCompanyClient(CompanyClient client) { return createCompanyClient.save(client); }
    public CompanyClient updateCompanyClient(CompanyClient client) { return updateCompanyClient.update(client); }
    public void deleteCompanyClientById(Long id) { deleteCompanyClient.deleteById(id); }
}
