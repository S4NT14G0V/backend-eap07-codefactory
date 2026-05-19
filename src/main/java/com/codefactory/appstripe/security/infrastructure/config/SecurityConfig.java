package com.codefactory.appstripe.security.infrastructure.config;

import com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilter;
import com.codefactory.appstripe.security.infrastructure.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Set<String> CSRF_SAFE_METHODS = Set.of("GET", "HEAD", "TRACE", "OPTIONS");
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";

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
                        .requireCsrfProtectionMatcher(cookieSessionCsrfMatcher())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/api/v1/transactions/**").permitAll()
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

    private RequestMatcher cookieSessionCsrfMatcher() {
        return request -> !CSRF_SAFE_METHODS.contains(request.getMethod())
                && hasSessionCookie(request.getCookies());
    }

    private boolean hasSessionCookie(Cookie[] cookies) {
        if (cookies == null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                return true;
            }
        }

        return false;
    }
}
