package com.codefactory.appstripe.security.aplication;

import com.codefactory.appstripe.security.aplication.port.TwoFactorPort;
import com.codefactory.appstripe.security.domain.User;

public class TwoFactorService {

    private final TwoFactorPort twoFactorPort;

    public TwoFactorService(TwoFactorPort twoFactorPort) {
        this.twoFactorPort = twoFactorPort;
    }

    public boolean verifyCode(User user, int code) {
        return twoFactorPort.verify(user.getTwoFactorSecret(), code);
    }
}

