package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.UserEntity;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.models.UserStatus;

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
        entity.setStatus((model.getUserStatus() != null ? model.getUserStatus() : UserStatus.ACTIVE).name());

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
        if (entity.getStatus() != null && !entity.getStatus().isBlank()) {
            model.setUserStatus(resolveStatus(entity.getStatus()));
        } else {
            model.setUserStatus(UserStatus.ACTIVE);
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
        try {
            return SystemRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Unknown system role stored in database: '" + role + "'. "
                + "Valid roles: NATURAL_CLIENT, COMPANY_CLIENT, TELLER_EMPLOYEE, "
                + "COMMERCIAL_EMPLOYEE, COMPANY_EMPLOYEE, COMPANY_SUPERVISOR, INTERNAL_ANALYST");
        }
    }

    private static UserStatus resolveStatus(String status) {
        try {
            return UserStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Unknown user status stored in database: '" + status + "'. "
                + "Valid statuses: ACTIVE, INACTIVE, BLOCKED");
        }
    }
}