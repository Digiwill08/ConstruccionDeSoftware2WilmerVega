package gestiondeunbanco.wilmervega.application.adapters.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
    List<AuditLogDocument> findByAffectedProductId(String affectedProductId);
    List<AuditLogDocument> findByUserId(Long userId);
}
