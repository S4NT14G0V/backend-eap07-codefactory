package com.codefactory.appstripe.security.aplication.port;

public interface TwoFactorPort {
    
    String generateSecret();
    boolean verify(String secret, int code);
}
