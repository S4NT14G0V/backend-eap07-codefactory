package com.codefactory.appstripe.security.application.port;

public interface IPasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
