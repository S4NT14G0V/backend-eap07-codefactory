package com.codefactory.appstripe.identity.infrastructure.persistence.entity;

import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "api_credentials")
public class ApiCredentialJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @Column(name = "secret_hash", nullable = false)
    private String secretHash;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "permission", nullable = false)
    private ApiCredentialPermission permission;
}
