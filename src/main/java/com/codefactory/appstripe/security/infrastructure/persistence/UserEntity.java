package com.codefactory.appstripe.security.infrastructure.persistence;

@Entity
public class UserEntity {
    private String username;
    private String password;
    private String twoFactorSecret;
}