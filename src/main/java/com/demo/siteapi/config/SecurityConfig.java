package com.demo.siteapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Site Assessment API.
 * <p>
 * Protects all {@code /api/**} endpoints with HTTP Basic authentication.
 * Swagger UI, OpenAPI specification endpoints, and the H2 console are
 * publicly accessible for demonstration purposes.
 * <p>
 * <b>Production note:</b> HTTP Basic with an in-memory user is not
 * suitable for production.  A real deployment would replace this with
 * OAuth2/OIDC/JWT and a proper user store.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the HTTP security filter chain.
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the built {@link SecurityFilterChain}
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(
                        headers -> headers.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    /**
     * Provides a shared {@link PasswordEncoder} bean.
     * <p>
     * Uses Spring Security’s {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     * which supports multiple hashing algorithms and defaults to bcrypt.
     *
     * @return a delegating password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Creates an in-memory user for demonstration.
     * <p>
     * The password is encoded using the application’s {@link PasswordEncoder}.
     *
     * @return an {@link InMemoryUserDetailsManager} containing a single user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}