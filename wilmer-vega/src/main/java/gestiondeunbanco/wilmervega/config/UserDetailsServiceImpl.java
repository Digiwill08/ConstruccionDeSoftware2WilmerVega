package gestiondeunbanco.wilmervega.config;

import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.services.FindUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service that integrates Spring Security with our Domain User Service.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FindUser findUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = findUser.findByUsername(username);
            
            // Note: In a production app, we would use a PasswordEncoder. 
            // For this project, we assume passwords stored as provided.
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password("{noop}" + user.getPassword()) // {noop} is for plain text passwords (dev only)
                    .roles(user.getSystemRole().name().replace("ROLE_", "")) // roles() adds ROLE_ prefix
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
