package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

public class DeleteUser {

    private final UserPort userPort;

    public DeleteUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public void deleteById(Long id) {
        if (userPort.findById(id).isEmpty()) {
            throw new NotFoundException("Cannot delete: User not found with ID " + id);
        }
        userPort.deleteById(id);
    }
}
