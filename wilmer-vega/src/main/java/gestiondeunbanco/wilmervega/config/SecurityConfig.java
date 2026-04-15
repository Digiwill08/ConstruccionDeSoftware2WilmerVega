package gestiondeunbanco.wilmervega.config;

import gestiondeunbanco.wilmervega.config.security.JwtAuthenticationFilter;
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
                // Public landing page
                .requestMatchers("/").permitAll()
                // Auth: login endpoint open to all users
                .requestMatchers("/auth/**").permitAll()
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
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

