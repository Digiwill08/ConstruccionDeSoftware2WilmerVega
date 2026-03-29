package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import gestiondeunbanco.wilmervega.domain.services.AuditLogDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogUseCase {

    private final AuditLogDomainService auditLogDomainService;

    public AuditLogUseCase(AuditLogPort auditLogPort) {
        this.auditLogDomainService = new AuditLogDomainService(auditLogPort);
    }

    public List<AuditLog> findAll() {
        return auditLogDomainService.findAll();
    }

    public AuditLog save(AuditLog auditLog) {
        return auditLogDomainService.save(auditLog);
    }

    public List<AuditLog> findByUserId(Long userId) {
        return auditLogDomainService.findByUserId(userId);
    }

    public List<AuditLog> findByAffectedProductId(String productId) {
        return auditLogDomainService.findByAffectedProductId(productId);
    }
}
