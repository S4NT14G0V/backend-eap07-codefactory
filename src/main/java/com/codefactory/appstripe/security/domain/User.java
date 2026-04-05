package com.codefactory.appstripe.security.domain;

public class User {
    private String username;
    private String password;
    private String twoFactorSecret;
    private boolean twoFactorEnabled;

    // Constructors, getters, and setters

    // Constructor para crear un nuevo usuario con 2FA deshabilitado por defecto
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.twoFactorSecret = "0"; // No se asigna un secreto 2FA por defecto
        this.twoFactorEnabled = false; // 2FA deshabilitado por defecto
    }

    // Constructor para reconstruir un usuario existente (por ejemplo, desde la base de datos)
    public User(String username, String password, String twoFactorSecret, boolean twoFactorEnabled){
        this.username = username;
        this.password = password;
        this.twoFactorSecret = twoFactorSecret;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    // Contructor para crear un nuevo usuario con 2FA habilitado
    public User(String username, String password, String twoFactorSecret) {
        this.username = username;
        this.password = password;
        this.twoFactorSecret = twoFactorSecret; // Se asigna el secreto 2FA proporcionado
        this.twoFactorEnabled = true; // 2FA habilitado por defecto si se proporciona un secreto
    }

    // Getters y setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

}


