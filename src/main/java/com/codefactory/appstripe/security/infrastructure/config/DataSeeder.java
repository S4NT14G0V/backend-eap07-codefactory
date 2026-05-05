package com.codefactory.appstripe.security.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codefactory.appstripe.security.application.port.IPasswordEncoderPort;
import com.codefactory.appstripe.security.application.port.IUserRepositoryPort;
import com.codefactory.appstripe.security.application.port.TwoFactorPort;
import com.codefactory.appstripe.security.domain.User;

@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    public CommandLineRunner initData(IUserRepositoryPort userRepository,
                                      IPasswordEncoderPort passwordEncoder,
                                      TwoFactorPort twoFactorPort) {
        return args -> {
            String adminEmail = "admin@paycore.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .accountActivated(true)
                        .twoFactorEnabled(false)
                        .build();
                userRepository.save(admin);
            }

            // Seed a test user with 2FA enabled for manual testing
            String testEmail = "2fa-test@paycore.com";
            if (!userRepository.existsByEmail(testEmail)) {
                String secret = twoFactorPort.generateSecret();
                User u = User.builder()
                        .email(testEmail)
                        .password(passwordEncoder.encode("Test1234!"))
                        .role("MERCHANT")
                        .accountActivated(true)
                        .twoFactorEnabled(true)
                        .twoFactorSecret(secret)
                        .build();
                userRepository.save(u);
                log.info("Seeded 2FA test user: {} password: {} twoFactorSecret: {}", testEmail, "Test1234!", secret);
            }
        };
    }
}
