package com.codefactory.appstripe.transactions.domain;

import com.codefactory.appstripe.transactions.domain.exception.InvalidTransactionStateException;

import java.math.BigDecimal;

public class Transaction {

    private String id; // id de la transaccion
    private String merchantId; // id del comercio o dueño de la transaccion
    private BigDecimal amount; // monto de la transaccion
    private TransactionStatus status; // estado de la transaccion, se asigna automaticamente a CREATED

    /*Constructor #1 para crear una transaccion COMPLETAMENTE NUEVA
    el estado inicial de las transacciones es automáticamente asignado a CREATED*/
    public Transaction(String id, String merchantId, BigDecimal amount) {
        this.id = id;
        this.merchantId = merchantId;
        this.amount = amount;
        this.status = TransactionStatus.CREATED; // Se asigna automáticamente al nacer
    }

    /*Constructor 2 Para reconstruir una transacción que ya existe en la base de datos
    lo usara mapper para el paquete de infraestructura
     */
    public Transaction(String id, String merchantId, BigDecimal amount, TransactionStatus status) {
        this.id = id;
        this.merchantId = merchantId;
        this.amount = amount;
        this.status = status;
    }

    //bloquea transición si está en estado final y cambia a PROCESSING.
    public void startProcessing() {
        // si ya está aprovado, rechazado o fallido (estados finales) no se puede cambiar su estado a processing
        if (this.status == TransactionStatus.APPROVED || this.status == TransactionStatus.REJECTED || this.status == TransactionStatus.FAILED) {
            throw new InvalidTransactionStateException(
                    "Operación bloqueada: No se puede cambiar a PROCESSING un pago que ya está en estado final (" + this.status + ")."
            );
        }
        // Si no está en estado final, se puede cambiar a PROCESSING
        this.status = TransactionStatus.PROCESSING;
    }

    // getters, pero se puede usan @Data de lombok

    public String getId() {
        return id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
