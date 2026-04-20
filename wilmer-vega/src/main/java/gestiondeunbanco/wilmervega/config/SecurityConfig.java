package gestiondeunbanco.wilmervega.config;

import gestiondeunbanco.wilmervega.config.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration with role-based access control.
 * Each API group is protected by the corresponding SystemRole.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/index.html", "/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/admin/**", "/api/analyst/**").hasAnyRole("INTERNAL_ANALYST", "ADMINISTRATOR")
                .requestMatchers("/api/supervisor/**").hasAnyRole("COMPANY_SUPERVISOR", "ADMINISTRATOR")
                .requestMatchers("/api/employee/**").hasAnyRole("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST", "ADMINISTRATOR")
                .requestMatchers("/api/client/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE", "ADMINISTRATOR")
                .requestMatchers("/api/customers/**").hasAnyRole("INTERNAL_ANALYST", "COMMERCIAL_EMPLOYEE", "ADMINISTRATOR")
                .requestMatchers("/api/accounts/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE", "TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST", "ADMINISTRATOR")
                .requestMatchers("/api/audit-logs/**").hasAnyRole("INTERNAL_ANALYST", "ADMINISTRATOR")
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\":401,\"message\":\"No autenticado: se requiere un token valido\",\"errors\":null}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\":403,\"message\":\"Acceso denegado: no tiene permisos para este recurso\",\"errors\":null}");
                })
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

