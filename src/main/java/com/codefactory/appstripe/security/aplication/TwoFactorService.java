package com.codefactory.appstripe.security.aplication;

import org.springframework.stereotype.Service;

import com.codefactory.appstripe.security.aplication.port.TwoFactorPort;

@Service
public class TwoFactorService {

    private final TwoFactorPort twoFactorPort;

    public TwoFactorService(TwoFactorPort twoFactorPort) {
        this.twoFactorPort = twoFactorPort;
    }

    public String generateSecret() {
        return twoFactorPort.generateSecret();
    }

    public boolean verifyCode(String twoFactorSecret, int code) {
        return twoFactorPort.verify(twoFactorSecret, code);
    }
}

