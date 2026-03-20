package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogPort AuditLogPort;

    public List<AuditLog> findAll() {
        return AuditLogPort.findAll();
    }

    public AuditLog save(AuditLog auditLog) {
        return AuditLogPort.save(auditLog);
    }

    public List<AuditLog> findByUserId(Long userId) {
        return AuditLogPort.findByUserId(userId);
    }

    public List<AuditLog> findByAffectedProductId(String productId) {
        return AuditLogPort.findByAffectedProductId(productId);
    }
}
