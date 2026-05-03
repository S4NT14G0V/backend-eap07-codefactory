package com.codefactory.appstripe.security.application.port;

public interface TwoFactorPort {
    String generateSecret();
    boolean verify(String secret, int code);
}
