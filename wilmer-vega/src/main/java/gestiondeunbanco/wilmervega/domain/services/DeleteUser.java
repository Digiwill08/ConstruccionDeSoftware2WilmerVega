package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.ports.UserPort;

public class DeleteUser {

    private final UserPort userPort;

    public DeleteUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public void deleteById(Long id) {
        userPort.deleteById(id);
    }
}
