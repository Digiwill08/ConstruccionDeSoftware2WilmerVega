package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.models.UserStatus;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

public class CreateUser {

    private final UserPort userPort;

    public CreateUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (userPort.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (user.getSystemRole() == null) {
            throw new IllegalArgumentException("System role is required");
        }

        if (requiresRelatedClient(user.getSystemRole())) {
            if (user.getRelatedClient() == null || user.getRelatedClient().getId() == null) {
                throw new IllegalArgumentException("Related client ID is required for this role");
            }
        }

        if (user.getUserStatus() == null) {
            user.setUserStatus(UserStatus.ACTIVE);
        }

        return userPort.save(user);
    }

    private boolean requiresRelatedClient(SystemRole role) {
        return role == SystemRole.NATURAL_CLIENT
                || role == SystemRole.COMPANY_CLIENT
                || role == SystemRole.COMPANY_EMPLOYEE
                || role == SystemRole.COMPANY_SUPERVISOR;
    }
}
