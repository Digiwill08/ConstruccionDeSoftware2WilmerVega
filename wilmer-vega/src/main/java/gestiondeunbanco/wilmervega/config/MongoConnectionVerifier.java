package gestiondeunbanco.wilmervega.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoConnectionVerifier {

    private final MongoTemplate mongoTemplate;

    @Value("${app.mongodb.required:false}")
    private boolean mongodbRequired;

    @EventListener(ApplicationReadyEvent.class)
    public void verifyMongoConnection() {
        try {
            Document result = mongoTemplate.executeCommand(new Document("ping", 1));
            log.info("MongoDB conectado correctamente. Resultado ping: {}", result.toJson());
        } catch (Exception ex) {
            String message = "No se pudo conectar a MongoDB. Configura MONGODB_URI (y opcionalmente MONGODB_DATABASE).";
            if (mongodbRequired) {
                throw new IllegalStateException(message, ex);
            }
            log.warn("{} Se continua con funcionalidad degradada de bitacora.", message);
        }
    }
}
