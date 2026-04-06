package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;

import java.util.List;

public class FindAuditLog {

    private final AuditLogPort auditLogPort;

    public FindAuditLog(AuditLogPort auditLogPort) {
        this.auditLogPort = auditLogPort;
    }

    public List<AuditLog> findAll() {
        return auditLogPort.findAll();
    }

    public List<AuditLog> findByUserId(Long userId) {
        return auditLogPort.findByUserId(userId);
    }

    public List<AuditLog> findByAffectedProductId(String productId) {
        return auditLogPort.findByAffectedProductId(productId);
    }
}
