package com.codefactory.appstripe.security.infrastructure.config;

import com.codefactory.appstripe.security.application.port.IUserRepositoryPort;
import com.codefactory.appstripe.security.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(IUserRepositoryPort userRepository) {
        return args -> {
            String adminEmail = "admin@paycore.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK.Y3FzI/P6sL6KkEu") // admin123
                        .role("ADMIN")
                        .accountActivated(true)
                        .twoFactorEnabled(false)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
