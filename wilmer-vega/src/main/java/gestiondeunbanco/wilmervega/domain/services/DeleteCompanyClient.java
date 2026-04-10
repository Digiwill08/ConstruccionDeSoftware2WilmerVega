package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;

public class DeleteCompanyClient {

    private final CompanyClientPort companyClientPort;

    public DeleteCompanyClient(CompanyClientPort companyClientPort) {
        this.companyClientPort = companyClientPort;
    }

    public void deleteById(Long id) {
        if (companyClientPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: CompanyClient not found with ID " + id);
        }
        companyClientPort.deleteById(id);
    }
}
