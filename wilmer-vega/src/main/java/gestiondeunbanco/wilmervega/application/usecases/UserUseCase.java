package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;
import gestiondeunbanco.wilmervega.domain.services.UserDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserUseCase {

    private final UserDomainService userDomainService;

    public UserUseCase(UserPort userPort) {
        this.userDomainService = new UserDomainService(userPort);
    }

    public List<User> findAll() {
        return userDomainService.findAll();
    }

    public Optional<User> findById(Long id) {
        return userDomainService.findById(id);
    }

    public User save(User user) {
        return userDomainService.save(user);
    }

    public void deleteById(Long id) {
        userDomainService.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userDomainService.findByUsername(username);
    }
}
