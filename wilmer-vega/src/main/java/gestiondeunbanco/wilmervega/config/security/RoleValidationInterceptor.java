package gestiondeunbanco.wilmervega.config.security;

import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Validates that authenticated API requests carry a known system role.
 */
@Component
public class RoleValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            return true;
        }

        if (path.startsWith("/api/auth/")) {
            return true;
        }

        if (!ClientAccessContext.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":401,\"message\":\"No autenticado\",\"errors\":null}");
            return false;
        }

        String role = ClientAccessContext.getCurrentRole();
        if (role == null || role.isBlank()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":403,\"message\":\"Rol de sistema no encontrado\",\"errors\":null}");
            return false;
        }

        try {
            SystemRole.valueOf(role);
            return true;
        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":403,\"message\":\"Rol de sistema invalido\",\"errors\":null}");
            return false;
        }
    }
}
