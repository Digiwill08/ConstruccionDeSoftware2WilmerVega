package gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories;

import gestiondeunbanco.wilmervega.application.adapters.persistence.sql.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByUserId(Long userId);
    List<AuditLogEntity> findByAffectedProductId(String productId);
}
