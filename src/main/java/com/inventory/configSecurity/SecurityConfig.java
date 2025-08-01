package com.inventory.configSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider; // Import AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Import DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Ensure this is present
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; // Import UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List; // Import List

@Configuration
@EnableWebSecurity // Important for full web security features
@EnableMethodSecurity // Enable method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Renamed from jwtFilter for consistency
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserDetailsService userDetailsService; // Inject UserDetailsService

    // Constructor with all necessary dependencies
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthEntryPoint jwtAuthEntryPoint,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless REST API
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint)) // Handle unauthorized access (401)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll() // Allow unauthenticated access to auth endpoints (login, register)
                        .requestMatchers(HttpMethod.GET, "/products/**").hasAnyAuthority("ADMIN", "USER") // GET products for both ADMIN and USER
                        .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority("ADMIN") // Only ADMIN can POST (create) products
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("ADMIN") // Only ADMIN can PUT (update) products
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority("ADMIN") // Only ADMIN can DELETE products
                        // Add more specific role-based access control here if needed for other endpoints
                        // .requestMatchers("/admin/**").hasAuthority("ADMIN") // Example: Only ADMIN can access /admin paths
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
                )
                .authenticationProvider(authenticationProvider()) // Use the authenticationProvider bean
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before Spring's default filter

        return http.build();
    }

    // CORS Configuration Source Bean (tuned for React frontend)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Allow sending credentials (e.g., cookies if used, though less common with JWT in headers)
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // IMPORTANT: Your React frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Standard methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept")); // Essential headers for requests
        configuration.setExposedHeaders(List.of("Authorization")); // If your backend ever sends JWT in a custom response header. Usually not needed if sent in body.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this CORS config to all paths
        return source;
    }

    // PasswordEncoder Bean for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationProvider Bean to link UserDetailsService and PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Inject your CustomUserDetailsService here
        authProvider.setPasswordEncoder(passwordEncoder()); // Use the BCryptPasswordEncoder bean
        return authProvider;
    }

    // AuthenticationManager Bean to manage the authentication process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
