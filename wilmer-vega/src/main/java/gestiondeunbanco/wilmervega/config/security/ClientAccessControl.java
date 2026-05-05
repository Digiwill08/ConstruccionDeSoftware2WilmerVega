package gestiondeunbanco.wilmervega.config.security;

import java.lang.annotation.*;

/**
 * Annotation to mark controller methods that require client access validation.
 * When applied, the method will validate that the current user (if a client)
 * can only access their own resources.
 * 
 * The parameter name must be specified so the aspect knows which path variable
 * contains the client ID.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientAccessControl {
    /**
     * Name of the path variable or request parameter containing the client ID.
     * Example: "clientId", "id" (for GET /api/clients/{id})
     */
    String clientIdParam() default "clientId";
}
