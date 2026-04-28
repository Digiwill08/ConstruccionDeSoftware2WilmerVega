package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.CompanyClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity;
import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;

public final class CompanyClientMapper {

    private CompanyClientMapper() {
    }

    public static CompanyClientEntity toEntity(CompanyClient model) {
        CompanyClientEntity entity = new CompanyClientEntity();
        entity.setId(model.getId());
        entity.setDocumentNumber(model.getDocumentNumber());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setAddress(model.getAddress());
        entity.setBusinessName(model.getBusinessName());
        if (model.getLegalRepresentative() != null) {
            NaturalClientEntity representative = new NaturalClientEntity();
            representative.setId(model.getLegalRepresentative().getId());
            entity.setLegalRepresentative(representative);
        }
        return entity;
    }

    public static CompanyClient toModel(CompanyClientEntity entity) {
        CompanyClient model = new CompanyClient();
        model.setId(entity.getId());
        model.setDocumentNumber(entity.getDocumentNumber());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setAddress(entity.getAddress());
        model.setBusinessName(entity.getBusinessName());
        if (entity.getLegalRepresentative() != null) {
            NaturalClient representative = new NaturalClient();
            representative.setId(entity.getLegalRepresentative().getId());
            model.setLegalRepresentative(representative);
        }
        return model;
    }
}