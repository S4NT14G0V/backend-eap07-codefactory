package com.codefactory.appstripe.security.infrastructure.config;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.codefactory.appstripe.common.api.ErrorResponse;
import com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilter;
import com.codefactory.appstripe.security.infrastructure.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CredentialValidationFilter credentialValidationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CredentialValidationFilter credentialValidationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.credentialValidationFilter = credentialValidationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((request, response, accessDeniedException) ->
                    writeError(response, HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED", accessDeniedException.getMessage()))
                .authenticationEntryPoint((request, response, authException) ->
                    writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", authException.getMessage()))
            )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/security/csrf", "/api/v1/auth/**", "/api/v1/transactions/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/merchant-portal/**").hasRole("MERCHANT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(credentialValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder springPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        repository.setCookieCustomizer(cookie -> cookie
                .httpOnly(true)
                .secure(true)
        );
        return repository;
    }

    private void writeError(HttpServletResponse response, int status, String errorCode, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .details(List.of(message))
                .traceId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .build();

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}
