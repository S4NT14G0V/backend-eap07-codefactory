package com.codefactory.appstripe.security.infrastructure.adapter;

import com.codefactory.appstripe.security.aplication.port.TwoFactorPort;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthAdapter implements TwoFactorPort {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    @Override
    public String generateSecret() {
        return gAuth.createCredentials().getKey();
    }

    @Override
    public boolean verify(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}