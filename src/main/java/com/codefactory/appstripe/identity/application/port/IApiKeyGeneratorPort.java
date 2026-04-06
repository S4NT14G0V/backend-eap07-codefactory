package com.codefactory.appstripe.identity.application.port;

public interface IApiKeyGeneratorPort {
    String generatePublicId();
    String generateSecretKey();
    String hashSecret(String plainSecret);
}
