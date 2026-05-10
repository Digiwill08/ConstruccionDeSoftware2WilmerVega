package gestiondeunbanco.wilmervega.config.security;

import gestiondeunbanco.wilmervega.domain.services.ClientAccessValidationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * AOP Aspect for enforcing @ClientAccessControl annotations.
 * Validates that clients can only access their own data before
 * controller method execution.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ClientAccessControlAspect {

    private final ClientAccessValidationService accessValidationService;

    /**
     * Intercept methods annotated with @ClientAccessControl and validate access
     */
    @Before("@annotation(clientAccessControl)")
    public void validateClientAccess(JoinPoint joinPoint, ClientAccessControl clientAccessControl) {
        String clientIdParamName = clientAccessControl.clientIdParam();
        Long clientId = extractClientIdFromArgs(joinPoint, clientIdParamName);

        if (clientId != null) {
            accessValidationService.validateClientAccess(clientId);
        }
    }

    /**
     * Extract client ID from method arguments by parameter name
     */
    private Long extractClientIdFromArgs(JoinPoint joinPoint, String paramName) {
        try {
            Method method = getTargetMethod(joinPoint);
            if (method == null) return null;

            Parameter[] parameters = method.getParameters();
            Object[] args = joinPoint.getArgs();

            // Find parameter index by name from @PathVariable or @RequestParam
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                
                // Check @PathVariable
                PathVariable pathVar = param.getAnnotation(PathVariable.class);
                if (pathVar != null) {
                    String value = pathVar.value().isEmpty() ? param.getName() : pathVar.value();
                    if (value.equals(paramName) && i < args.length) {
                        return convertToLong(args[i]);
                    }
                }

                // Check @RequestParam
                RequestParam reqParam = param.getAnnotation(RequestParam.class);
                if (reqParam != null) {
                    String value = reqParam.value().isEmpty() ? param.getName() : reqParam.value();
                    if (value.equals(paramName) && i < args.length) {
                        return convertToLong(args[i]);
                    }
                }

                // Check by parameter name directly
                if (param.getName().equals(paramName) && i < args.length) {
                    return convertToLong(args[i]);
                }
            }
        } catch (Exception e) {
            // Log but don't fail on extraction errors - let controller handle
        }
        
        return null;
    }

    /**
     * Get target method from JoinPoint (handles proxied objects)
     */
    private Method getTargetMethod(JoinPoint joinPoint) {
        try {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            
            // Try to get method from target class
            for (Method method : targetClass.getMethods()) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Convert value to Long, handling null and String conversions
     */
    private Long convertToLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
