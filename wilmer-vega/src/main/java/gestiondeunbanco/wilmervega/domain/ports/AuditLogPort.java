package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import java.util.List;

public interface AuditLogPort {
    List<AuditLog> findAll();
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByAffectedProductId(String productId);
}
