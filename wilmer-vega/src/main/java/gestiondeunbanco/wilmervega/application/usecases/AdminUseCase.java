package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.services.*;

import java.util.List;
import java.util.Optional;

public class AdminUseCase {

    private final CreateUser createUser;
    private final FindUser findUser;
    private final DeleteUser deleteUser;
    private final FindAuditLog findAuditLog;

    public AdminUseCase(CreateUser createUser, FindUser findUser, DeleteUser deleteUser, FindAuditLog findAuditLog) {
        this.createUser = createUser;
        this.findUser = findUser;
        this.deleteUser = deleteUser;
        this.findAuditLog = findAuditLog;
    }

    public List<User> findAllUsers() { return findUser.findAll(); }
    public Optional<User> findUserById(Long id) { return findUser.findById(id); }
    public User saveUser(User user) { return createUser.save(user); }
    public void deleteUserById(Long id) { deleteUser.deleteById(id); }
    public Optional<User> findUserByUsername(String username) { return findUser.findByUsername(username); }

    public List<AuditLog> findAllAuditLogs() { return findAuditLog.findAll(); }
    public List<AuditLog> findAuditLogsByUserId(Long userId) { return findAuditLog.findByUserId(userId); }
    public List<AuditLog> findAuditLogsByAffectedProductId(String productId) { return findAuditLog.findByAffectedProductId(productId); }
}
