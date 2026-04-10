package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.NotFoundException;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

import java.util.List;

public class FindUser {

    private final UserPort userPort;

    public FindUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public List<User> findAll() {
        return userPort.findAll();
    }

    public User findById(Long id) {
        return userPort.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    public User findByUsername(String username) {
        return userPort.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }
}
