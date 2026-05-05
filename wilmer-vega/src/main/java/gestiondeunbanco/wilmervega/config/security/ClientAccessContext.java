package gestiondeunbanco.wilmervega.config.security;

import lombok.Getter;

/**
 * Thread-local context for storing current user's client information.
 * Enables access control validation in service layer and controllers.
 */
@Getter
public class ClientAccessContext {
    private static final ThreadLocal<ClientAccessContext> context = new ThreadLocal<>();

    private Long userId;
    private Long clientId;
    private String role;

    private ClientAccessContext(Long userId, Long clientId, String role) {
        this.userId = userId;
        this.clientId = clientId;
        this.role = role;
    }

    /**
     * Set the current context for this thread
     */
    public static void setContext(Long userId, Long clientId, String role) {
        context.set(new ClientAccessContext(userId, clientId, role));
    }

    /**
     * Get the current context (may be null if not authenticated)
     */
    public static ClientAccessContext getCurrentContext() {
        return context.get();
    }

    /**
     * Get current user ID (null if not authenticated)
     */
    public static Long getCurrentUserId() {
        ClientAccessContext ctx = context.get();
        return ctx != null ? ctx.userId : null;
    }

    /**
     * Get current client ID (null if user is not a client or not authenticated)
     */
    public static Long getCurrentClientId() {
        ClientAccessContext ctx = context.get();
        return ctx != null ? ctx.clientId : null;
    }

    /**
     * Get current user role (null if not authenticated)
     */
    public static String getCurrentRole() {
        ClientAccessContext ctx = context.get();
        return ctx != null ? ctx.role : null;
    }

    /**
     * Check if current user is authenticated
     */
    public static boolean isAuthenticated() {
        return context.get() != null;
    }

    /**
     * Check if current user is a client (has clientId)
     */
    public static boolean isClient() {
        ClientAccessContext ctx = context.get();
        return ctx != null && ctx.clientId != null;
    }

    /**
     * Clear the context (call from filter cleanup)
     */
    public static void clear() {
        context.remove();
    }
}
