package com.codefactory.appstripe.security.aplication;

import com.codefactory.appstripe.security.domain.User;
import com.codefactory.appstripe.security.aplication.port.TwoFactorPort;

public class TwoFactorService {

    private final TwoFactorPort twoFactorPort;

    public boolean verifyCode(User user, int code) {
        return twoFactorPort.verify(user.getTwoFactorSecret(), code);
    }
}

