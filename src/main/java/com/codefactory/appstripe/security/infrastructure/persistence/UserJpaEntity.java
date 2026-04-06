package com.codefactory.appstripe.security.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    // Constructor vacío (obligatorio para JPA)
    public UserJpaEntity() {}

    // Constructor completo
    public UserJpaEntity(String username, String password, String twoFactorSecret, boolean twoFactorEnabled) {
        this.username = username;
        this.password = password;
        this.twoFactorSecret = twoFactorSecret;
        this.twoFactorEnabled = twoFactorEnabled;
    }
}