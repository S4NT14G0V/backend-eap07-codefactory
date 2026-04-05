package com.codefactory.appstripe.transactions.application.port;

import com.codefactory.appstripe.transactions.domain.Transaction;


// el sistema notifica al comercio el inicio del procesamiento del pago
public interface IMerchantNotifierPort {

    // Envía la notificación al comercio
    void notifyProcessingStart(Transaction transaction);
}