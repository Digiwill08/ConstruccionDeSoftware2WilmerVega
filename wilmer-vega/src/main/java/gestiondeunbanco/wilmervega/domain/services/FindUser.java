package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

import java.util.List;
import java.util.Optional;

public class FindUser {

    private final UserPort userPort;

    public FindUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public List<User> findAll() {
        return userPort.findAll();
    }

    public Optional<User> findById(Long id) {
        return userPort.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userPort.findByUsername(username);
    }
}
