package com.codefactory.appstripe.transactions.application.port;

import com.codefactory.appstripe.transactions.domain.TransactionStatus;



// el sistema registra el cambio de estado en la bitácora de auditoría indicando el estado anterior y el nuevo
public interface IAuditPublisherPort {

    // Registra en auditoría el cambio de estado de un pago
    void publishStatusChange(String transactionId, TransactionStatus oldStatus, TransactionStatus newStatus);
}