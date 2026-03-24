package gestiondeunbanco.wilmervega.domain.repository;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByAffectedProductId(String productId);
}
