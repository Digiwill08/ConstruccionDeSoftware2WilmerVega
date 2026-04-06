package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;

public class CreateAuditLog {

    private final AuditLogPort auditLogPort;

    public CreateAuditLog(AuditLogPort auditLogPort) {
        this.auditLogPort = auditLogPort;
    }

    public AuditLog save(AuditLog auditLog) {
        if (auditLog == null) {
            throw new IllegalArgumentException("AuditLog cannot be null");
        }
        return auditLogPort.save(auditLog);
    }
}
