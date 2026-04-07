package com.codefactory.appstripe.identity.infrastructure.persistence.entity;

import com.codefactory.appstripe.identity.domain.MerchantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "merchants")
public class MerchantJpaEntity {

    @Id
    private String id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_id", nullable = false, unique = true)
    private String businessId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MerchantStatus status;
}
