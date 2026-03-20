package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogPersistenceAdapter implements AuditLogPort {
    private final AuditLogRepository repository;

    @Override public List<AuditLog> findAll() { return repository.findAll(); }
    @Override public AuditLog save(AuditLog auditLog) { return repository.save(auditLog); }
    @Override public List<AuditLog> findByUserId(Long userId) { return repository.findByUserId(userId); }
    @Override public List<AuditLog> findByAffectedProductId(String productId) { return repository.findByAffectedProductId(productId); }
}
