package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.UserEntity;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.models.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserEntity toEntity(User model) {
        UserEntity entity = new UserEntity();
        entity.setId(model.getUserId());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        if (model.getSystemRole() != null) {
            entity.setRole(model.getSystemRole().name());
        }

        if (model.getRelatedClient() != null) {
            ClientEntity client = new ClientEntity();
            client.setId(model.getRelatedClient().getId());
            entity.setClient(client);
        }
        return entity;
    }

    public static User toModel(UserEntity entity) {
        User model = new User();
        model.setUserId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPassword(entity.getPassword());
        if (entity.getRole() != null) {
            model.setSystemRole(resolveRole(entity.getRole()));
        }

        if (entity.getClient() != null) {
            NaturalClient client = new NaturalClient();
            client.setId(entity.getClient().getId());
            client.setEmail(entity.getClient().getEmail());
            client.setPhone(entity.getClient().getPhone());
            client.setAddress(entity.getClient().getAddress());
            client.setDocumentNumber(entity.getClient().getDocumentNumber());
            model.setRelatedClient(client);
        }
        return model;
    }

    private static SystemRole resolveRole(String role) {
        if ("ADMINISTRATOR".equalsIgnoreCase(role)) {
            return SystemRole.INTERNAL_ANALYST;
        }
        return SystemRole.valueOf(role);
    }
}