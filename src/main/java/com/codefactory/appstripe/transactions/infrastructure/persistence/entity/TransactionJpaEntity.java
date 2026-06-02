package com.codefactory.appstripe.transactions.infrastructure.persistence.entity;


import java.math.BigDecimal;

import com.codefactory.appstripe.transactions.domain.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    private String id;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // Guarda el Enum como texto (ej. "CREATED") y no como un número (0, 1)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    public TransactionJpaEntity() {
    }

    // Constructor
    public TransactionJpaEntity(String id, String merchantId, BigDecimal amount, TransactionStatus status) {
        this.id = id;
        this.merchantId = merchantId;
        this.amount = amount;
        this.status = status;
    }


}
