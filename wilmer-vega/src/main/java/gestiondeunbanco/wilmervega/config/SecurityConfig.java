package gestiondeunbanco.wilmervega.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration with role-based access control.
 * Each API group is protected by the corresponding SystemRole.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                // Admin: full system users and audit management
                .requestMatchers("/api/admin/**").hasRole("INTERNAL_ANALYST")
                // Analyst: loan lifecycle and audit log
                .requestMatchers("/api/analyst/**").hasRole("INTERNAL_ANALYST")
                // Supervisor: approve/reject company transfers
                .requestMatchers("/api/supervisor/**").hasRole("COMPANY_SUPERVISOR")
                // Employee: bank operations (accounts, clients, loans creation)
                .requestMatchers("/api/employee/**")
                    .hasAnyRole("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST")
                // Client: own accounts and transfers
                .requestMatchers("/api/client/**")
                    .hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE")
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

