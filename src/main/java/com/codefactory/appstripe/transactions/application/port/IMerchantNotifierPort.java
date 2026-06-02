package com.codefactory.appstripe.transactions.application.port;

import java.math.BigDecimal;

import com.codefactory.appstripe.transactions.domain.Transaction;


// el sistema notifica al comercio el inicio del procesamiento del pago
public interface IMerchantNotifierPort {

    // Envía la notificación al comercio
    void notifyProcessingStart(Transaction transaction);
    
    // Notifica el resultado final del procesamiento al comercio
    void notifyProcessingCompletion(Transaction transaction, String result, String authorizationCode, String rejectionReason);

    // Nuevo para HU016/HU017
    void notifyRefund(Transaction transaction, BigDecimal refundedAmount, String reason);
}