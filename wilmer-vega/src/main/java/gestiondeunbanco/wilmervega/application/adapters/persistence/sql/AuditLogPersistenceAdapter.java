package gestiondeunbanco.wilmervega.application.adapters.persistence.sql;

import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.models.OperationType;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories.AuditLogRepository;
import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.AuditLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditLogPersistenceAdapter implements AuditLogPort {

    private final AuditLogRepository repository;

    @Override
    public List<AuditLog> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity entity = toEntity(auditLog);
        return toModel(repository.save(entity));
    }

    @Override
    public List<AuditLog> findByUserId(Long userId) {
        return repository.findAll().stream().filter(a->a.getUserId().equals(userId)).map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByAffectedProductId(String productId) {
        return repository.findAll().stream().filter(a->a.getAffectedProductId().equals(productId)).map(this::toModel).collect(Collectors.toList());
    }

    private AuditLogEntity toEntity(AuditLog model) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setAuditLogId(model.getAuditLogId());
        if (model.getOperationType() != null) entity.setOperationType(model.getOperationType().name());
        entity.setOperationDateTime(model.getOperationDateTime());
        entity.setUserId(model.getUserId());
        entity.setUserRole(model.getUserRole());
        entity.setAffectedProductId(model.getAffectedProductId());
        return entity;
    }

    private AuditLog toModel(AuditLogEntity entity) {
        AuditLog model = new AuditLog();
        model.setAuditLogId(entity.getAuditLogId());
        if (entity.getOperationType() != null) model.setOperationType(OperationType.valueOf(entity.getOperationType()));
        model.setOperationDateTime(entity.getOperationDateTime());
        model.setUserId(entity.getUserId());
        model.setUserRole(entity.getUserRole());
        model.setAffectedProductId(entity.getAffectedProductId());
        return model;
    }
}
