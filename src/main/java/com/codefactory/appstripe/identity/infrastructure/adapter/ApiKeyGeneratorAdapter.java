package com.codefactory.appstripe.identity.infrastructure.adapter;

import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class ApiKeyGeneratorAdapter implements IApiKeyGeneratorPort {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String generateSecretKey() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return "sk_live_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public String hashSecret(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No fue posible generar hash para la credencial", e);
        }
    }

    @Override
    public String generatePublicId() {
        return "pk_live_" + UUID.randomUUID().toString().replace("-", "");
    }
}
