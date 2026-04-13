package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;

import java.util.List;

/**
 * Output port for the NoSQL (MongoDB) audit log.
 * All transactional domain services must use this port to register operations.
 */
public interface AuditLogMongoPort {
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByAffectedProductId(String productId);
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findAll();
}
