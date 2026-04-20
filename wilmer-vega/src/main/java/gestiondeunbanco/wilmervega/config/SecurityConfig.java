package gestiondeunbanco.wilmervega.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

/**
 * Security configuration with role-based access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/api/admin/**").hasRole("INTERNAL_ANALYST")
                .requestMatchers("/api/analyst/**").hasRole("INTERNAL_ANALYST")
                .requestMatchers("/api/supervisor/**").hasRole("COMPANY_SUPERVISOR")
                .requestMatchers("/api/employee/**")
                    .hasAnyRole("TELLER_EMPLOYEE", "COMMERCIAL_EMPLOYEE", "INTERNAL_ANALYST")
                .requestMatchers("/api/client/**")
                    .hasAnyRole("NATURAL_CLIENT", "COMPANY_CLIENT", "COMPANY_EMPLOYEE")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
