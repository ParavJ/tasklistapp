package com.example.tasklistapi.config;

import com.example.tasklistapi.model.User;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.tasklistapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @Configuration: Marks this class as a source of bean definitions for the Spring application context.
 * @EnableWebSecurity: Enables Spring Security's web security support and MVC integration.
 * @RequiredArgsConstructor: A Lombok annotation to create a constructor for all final fields (e.g., userRepository).
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    /**
     * This bean defines the primary password hashing mechanism for the application.
     * By defining this bean, we tell Spring Security which algorithm to use for password verification.
     * It's a fundamental building block for security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This bean defines how to fetch user data. It's the bridge between our custom user model
     * (stored in the database) and Spring Security's internal user representation (UserDetails).
     * This is the second fundamental building block that Spring's auto-configuration looks for.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Fetch our application's user entity from the repository.
            User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // Convert our user entity into a Spring Security UserDetails object.
            // This is the format Spring Security understands.
            return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(), // Password here is the hashed password from the database.
                // Convert our Role enum to a collection of GrantedAuthority for authorization.
                List.of(new SimpleGrantedAuthority(appUser.getRole().name()))
            );
        };
    }
    
    /**
     * The AuthenticationManager is the core component that processes an authentication request.
     * Spring Boot automatically configures it using the UserDetailsService and PasswordEncoder beans we provided.
     * We expose it as a bean here so we can inject it into our AuthController for our manual login endpoint.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * This bean configures the application's security filter chain, which acts as a firewall.
     * It defines the security rules for all incoming HTTP requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable Cross-Site Request Forgery (CSRF). It's not necessary for stateless REST APIs.
            .csrf(AbstractHttpConfigurer::disable)
            
            // This section defines the authorization rules for our API endpoints.
            .authorizeHttpRequests(auth -> auth
                // Rule 1: Allow all requests to endpoints under "/api/auth/". This is our public endpoint for login/signup.
                .requestMatchers("/api/auth/**").permitAll()
                // Rule 2: Any other request to the application must be authenticated.
                .anyRequest().authenticated()
            )
            
            // Configure session management to be STATELESS. The server will not create or hold any user session data.
            // Every request must be re-authenticated, which is why we will use JWTs.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // NOTE: The call to .authenticationProvider() is no longer needed here.
        // Because we provided UserDetailsService and PasswordEncoder beans, Spring Boot's auto-configuration
        // creates the DaoAuthenticationProvider and registers it for us.
            
        // In the upcoming steps, we will add our custom JWT authentication filter into this chain.
            
        return http.build();
    }
}