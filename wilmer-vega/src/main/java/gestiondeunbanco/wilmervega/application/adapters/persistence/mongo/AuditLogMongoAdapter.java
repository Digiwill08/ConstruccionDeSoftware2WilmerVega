package gestiondeunbanco.wilmervega.application.adapters.persistence.mongo;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogMongoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter that implements AuditLogMongoPort using Spring Data MongoDB.
 * Maps between AuditLog (domain model) and AuditLogDocument (MongoDB entity).
 */
@Component
@RequiredArgsConstructor
public class AuditLogMongoAdapter implements AuditLogMongoPort {

    private final AuditLogMongoRepository repository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogDocument doc = toDocument(auditLog);
        AuditLogDocument saved = repository.save(doc);
        return toDomain(saved);
    }

    @Override
    public List<AuditLog> findByAffectedProductId(String productId) {
        return repository.findByAffectedProductId(productId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // --- Mappers ---

    private AuditLogDocument toDocument(AuditLog domain) {
        AuditLogDocument doc = new AuditLogDocument();
        doc.setId(domain.getAuditLogId());
        doc.setOperationType(domain.getOperationType());
        doc.setOperationDateTime(domain.getOperationDateTime());
        doc.setUserId(domain.getUserId());
        doc.setUserRole(domain.getUserRole());
        doc.setAffectedProductId(domain.getAffectedProductId());
        doc.setDetails(domain.getDetails());
        return doc;
    }

    private AuditLog toDomain(AuditLogDocument doc) {
        AuditLog domain = new AuditLog();
        domain.setAuditLogId(doc.getId());
        domain.setOperationType(doc.getOperationType());
        domain.setOperationDateTime(doc.getOperationDateTime());
        domain.setUserId(doc.getUserId());
        domain.setUserRole(doc.getUserRole());
        domain.setAffectedProductId(doc.getAffectedProductId());
        domain.setDetails(doc.getDetails());
        return domain;
    }
}
