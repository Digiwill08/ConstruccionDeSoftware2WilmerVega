package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.UserPort;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.models.UserStatus;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.UserRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findAll().stream().filter(u->u.getUsername().equals(username)).findFirst().map(this::toModel);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return repository.findAll().stream().anyMatch(u->u.getUsername().equals(username));
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        return toModel(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private UserEntity toEntity(User model) {
        UserEntity entity = new UserEntity();
        entity.setId(model.getUserId());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        if (model.getSystemRole() != null) entity.setRole(model.getSystemRole().name());
        
        if (model.getRelatedClient() != null) {
            gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity c = new gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.ClientEntity();
            c.setId(model.getRelatedClient().getId());
            entity.setClient(c);
        }
        return entity;
    }

    private User toModel(UserEntity entity) {
        User model = new User();
        model.setUserId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPassword(entity.getPassword());
        if (entity.getRole() != null) model.setSystemRole(SystemRole.valueOf(entity.getRole()));
        
        if (entity.getClient() != null) {
            gestiondeunbanco.wilmervega.domain.models.NaturalClient c = new gestiondeunbanco.wilmervega.domain.models.NaturalClient();
            c.setId(entity.getClient().getId());
            model.setRelatedClient(c);
        }
        return model;
    }
}
