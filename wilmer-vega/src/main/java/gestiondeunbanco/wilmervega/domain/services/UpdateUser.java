package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

public class UpdateUser {

    private final UserPort userPort;

    public UpdateUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public User update(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("User or UserId cannot be null for update");
        }
        if (userPort.findById(user.getUserId()).isEmpty()) {
            throw new NotFoundException("User not found for update");
        }
        return userPort.save(user);
    }
}
