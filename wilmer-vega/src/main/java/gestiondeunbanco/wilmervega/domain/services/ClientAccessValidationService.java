package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.config.security.ClientAccessContext;
import gestiondeunbanco.wilmervega.domain.exceptions.UnauthorizedAccessException;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import org.springframework.stereotype.Service;

/**
 * Service for validating client access control at domain layer.
 * Ensures that clients can only access their own data.
 */
@Service
public class ClientAccessValidationService {

    /**
     * Validate that current user has access to the specified client.
     * Clients can only access their own data.
     * Internal employees and analysts have full access.
     * 
     * @param requestedClientId The ID of the client being accessed
     * @throws UnauthorizedAccessException if access is denied
     */
    public void validateClientAccess(Long requestedClientId) {
        if (requestedClientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }

        String currentRole = ClientAccessContext.getCurrentRole();
        Long currentClientId = ClientAccessContext.getCurrentClientId();

        // Internal roles bypass client access control
        if (isInternalRole(currentRole)) {
            return;
        }

        // Client roles must match
        if (!isClientAccess(currentRole)) {
            throw new UnauthorizedAccessException("User role does not have client access permissions");
        }

        // Validate client ownership
        if (currentClientId == null || !currentClientId.equals(requestedClientId)) {
            throw new UnauthorizedAccessException("Client A cannot access Client B's resources");
        }
    }

    /**
     * Validate that current user has access to account data for a specific client.
     * 
     * @param accountClientId The ID of the client who owns the account
     * @throws UnauthorizedAccessException if access is denied
     */
    public void validateAccountAccess(Long accountClientId) {
        if (accountClientId == null) {
            throw new IllegalArgumentException("Account client ID cannot be null");
        }

        String currentRole = ClientAccessContext.getCurrentRole();
        Long currentClientId = ClientAccessContext.getCurrentClientId();

        // Employees, analysts, and internal roles bypass access control
        if (isInternalRole(currentRole) || isBankEmployeeRole(currentRole)) {
            return;
        }

        // Client roles must match
        if (!isClientAccess(currentRole)) {
            throw new UnauthorizedAccessException("User role does not have account access permissions");
        }

        if (currentClientId == null || !currentClientId.equals(accountClientId)) {
            throw new UnauthorizedAccessException("Cannot access account from different client");
        }
    }

    /**
     * Check if current user is authenticated
     */
    public boolean isAuthenticated() {
        return ClientAccessContext.isAuthenticated();
    }

    /**
     * Check if current user is a client
     */
    public boolean isClient() {
        return ClientAccessContext.isClient();
    }

    /**
     * Get current user's client ID
     */
    public Long getCurrentClientId() {
        return ClientAccessContext.getCurrentClientId();
    }

    /**
     * Get current user's ID
     */
    public Long getCurrentUserId() {
        return ClientAccessContext.getCurrentUserId();
    }

    // ========== Private helper methods ==========

    /**
     * Check if role is internal (non-client) role
     */
    private boolean isInternalRole(String role) {
        return role != null && (
            role.equals(SystemRole.INTERNAL_ANALYST.name()) ||
            role.equals(SystemRole.COMPANY_SUPERVISOR.name()) ||
            role.equals(SystemRole.TELLER_EMPLOYEE.name()) ||
            role.equals(SystemRole.COMMERCIAL_EMPLOYEE.name())
        );
    }

    /**
     * Check if role is client-accessible role
     */
    private boolean isClientAccess(String role) {
        return role != null && (
            role.equals(SystemRole.NATURAL_CLIENT.name()) ||
            role.equals(SystemRole.COMPANY_CLIENT.name()) ||
            role.equals(SystemRole.COMPANY_EMPLOYEE.name())
        );
    }

    /**
     * Check if role is bank employee
     */
    private boolean isBankEmployeeRole(String role) {
        return role != null && (
            role.equals(SystemRole.TELLER_EMPLOYEE.name()) ||
            role.equals(SystemRole.COMMERCIAL_EMPLOYEE.name())
        );
    }
}
