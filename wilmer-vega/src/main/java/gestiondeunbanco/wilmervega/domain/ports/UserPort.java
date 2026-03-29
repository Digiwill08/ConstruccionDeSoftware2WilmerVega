package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserPort {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
