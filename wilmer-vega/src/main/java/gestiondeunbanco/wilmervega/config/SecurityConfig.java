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
                .requestMatchers("/", "/auth/**").permitAll()
                .requestMatchers("/api/admin/**", "/api/analyst/**").hasRole("INTERNAL_ANALYST")
                .requestMatchers("/api/supervisor/**").hasRole("COMPANY_SUPERVISOR")
                .requestMatchers("/api/employee/**").hasAnyRole("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST")
                .requestMatchers("/api/client/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE")
                .requestMatchers("/api/customers/**").hasAnyRole("INTERNAL_ANALYST", "COMMERCIAL_EMPLOYEE")
                .requestMatchers("/api/accounts/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE", "TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST")
                .requestMatchers("/api/loans/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST")
                .requestMatchers("/api/transfers/**").hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE", "COMPANY_SUPERVISOR", "INTERNAL_ANALYST")
                .requestMatchers("/api/audit-logs/**").hasRole("INTERNAL_ANALYST")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

