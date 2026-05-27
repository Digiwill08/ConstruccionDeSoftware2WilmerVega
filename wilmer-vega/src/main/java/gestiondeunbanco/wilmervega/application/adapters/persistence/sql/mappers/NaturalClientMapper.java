package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.NaturalClientEntity;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;

public final class NaturalClientMapper {

    private NaturalClientMapper() {
    }

    public static NaturalClientEntity toEntity(NaturalClient model) {
        NaturalClientEntity entity = new NaturalClientEntity();
        entity.setId(model.getId());
        entity.setDocumentNumber(model.getDocumentNumber());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setAddress(model.getAddress());
        entity.setFullName(model.getFullName());
        entity.setBirthDate(model.getBirthDate());
        if (model.getRole() != null) {
            entity.setRole(model.getRole().name());
        }
        return entity;
    }

    public static NaturalClient toModel(NaturalClientEntity entity) {
        NaturalClient model = new NaturalClient();
        model.setId(entity.getId());
        model.setDocumentNumber(entity.getDocumentNumber());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setAddress(entity.getAddress());
        model.setFullName(entity.getFullName());
        model.setBirthDate(entity.getBirthDate());
        if (entity.getRole() != null) {
            model.setRole(SystemRole.valueOf(entity.getRole()));
        }
        return model;
    }
}