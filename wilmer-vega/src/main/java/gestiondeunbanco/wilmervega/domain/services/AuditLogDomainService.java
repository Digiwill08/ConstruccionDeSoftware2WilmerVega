package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;

import java.util.List;

public class AuditLogDomainService {

    private final AuditLogPort auditLogPort;

    public AuditLogDomainService(AuditLogPort auditLogPort) {
        this.auditLogPort = auditLogPort;
    }

    public List<AuditLog> findAll() {
        return auditLogPort.findAll();
    }

    public AuditLog save(AuditLog auditLog) {
        if (auditLog == null) {
            throw new IllegalArgumentException("AuditLog cannot be null");
        }
        return auditLogPort.save(auditLog);
    }

    public List<AuditLog> findByUserId(Long userId) {
        return auditLogPort.findByUserId(userId);
    }

    public List<AuditLog> findByAffectedProductId(String productId) {
        return auditLogPort.findByAffectedProductId(productId);
    }
}
