package com.codefactory.appstripe.security.infrastructure.config;

import com.codefactory.appstripe.security.application.port.IPasswordEncoderPort;
import com.codefactory.appstripe.security.application.port.IUserRepositoryPort;
import com.codefactory.appstripe.security.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(IUserRepositoryPort userRepository, IPasswordEncoderPort passwordEncoder) {
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
        };
    }
}
