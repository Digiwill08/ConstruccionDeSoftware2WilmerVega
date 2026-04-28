package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.UserPort;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.UserRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserMapper::toModel).toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserMapper::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserMapper::toModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return UserMapper.toModel(repository.save(UserMapper.toEntity(user)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
