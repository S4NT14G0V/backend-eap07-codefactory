package com.codefactory.appstripe.security.infrastructure.adapter;

import org.springframework.stereotype.Component;

import com.codefactory.appstripe.security.application.port.TwoFactorPort;
import com.warrenstrange.googleauth.GoogleAuthenticator;

@Component
public class GoogleAuthAdapter implements TwoFactorPort {

    private final GoogleAuthenticator gAuth;

    public GoogleAuthAdapter(GoogleAuthenticator gAuth) {
        this.gAuth = gAuth;
    }

    @Override
    public String generateSecret() {
        return gAuth.createCredentials().getKey();
    }

    @Override
    public boolean verify(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}