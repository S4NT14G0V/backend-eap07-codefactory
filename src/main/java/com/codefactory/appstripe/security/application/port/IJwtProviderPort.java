package com.codefactory.appstripe.security.application.port;

import com.codefactory.appstripe.security.domain.User;

public interface IJwtProviderPort {
    String generateToken(User user);
    boolean validateToken(String token);
    String extractEmail(String token);
    String extractRole(String token);
    String extractMerchantId(String token);
}
