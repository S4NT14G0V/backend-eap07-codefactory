package com.codefactory.appstripe.transactions.infrastructure.adapter;

import org.springframework.stereotype.Component;

import com.codefactory.appstripe.transactions.application.port.IMerchantNotifierPort;
import com.codefactory.appstripe.transactions.domain.Transaction;

@Component
public class MerchantNotifierAdapter implements IMerchantNotifierPort {

    @Override
    public void notifyProcessingStart(Transaction transaction) {
        System.out.println("El procesamiento del pago con ID " + transaction.getId() + " ha comenzado.");
    }

    @Override
    public void notifyProcessingCompletion(Transaction transaction, String result, String authorizationCode, String rejectionReason) {
        System.out.println("Notificando resultado al comercio para transaccion " + transaction.getId() + ": result=" + result + ", authCode=" + authorizationCode + ", reason=" + rejectionReason);
    }

    @Override
    public void notifyRefund(Transaction transaction,
                              java.math.BigDecimal refundedAmount,
                              String reason) {
        System.out.println("Notificando reembolso al comercio para transaccion "
                + transaction.getId()
                + ": monto=" + refundedAmount
                + ", motivo=" + reason);
    }
}
