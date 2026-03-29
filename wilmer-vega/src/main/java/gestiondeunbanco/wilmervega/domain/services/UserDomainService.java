package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;

import java.util.List;
import java.util.Optional;

public class UserDomainService {

    private final UserPort userPort;

    public UserDomainService(UserPort userPort) {
        this.userPort = userPort;
    }

    public List<User> findAll() {
        return userPort.findAll();
    }

    public Optional<User> findById(Long id) {
        return userPort.findById(id);
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() != null && userPort.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userPort.save(user);
    }

    public void deleteById(Long id) {
        userPort.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userPort.findByUsername(username);
    }
}
