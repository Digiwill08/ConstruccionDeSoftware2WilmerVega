package gestiondeunbanco.wilmervega.infrastructure.adapters;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import gestiondeunbanco.wilmervega.infrastructure.adapters.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogPersistenceAdapter implements AuditLogPort {

    private final AuditLogRepository auditLogRepository;

    @Override
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLog> findByUserId(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }

    @Override
    public List<AuditLog> findByAffectedProductId(String productId) {
        return auditLogRepository.findByAffectedProductId(productId);
    }
}
