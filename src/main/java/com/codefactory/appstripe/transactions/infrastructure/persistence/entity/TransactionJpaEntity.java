package com.codefactory.appstripe.transactions.infrastructure.persistence.entity;


import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
