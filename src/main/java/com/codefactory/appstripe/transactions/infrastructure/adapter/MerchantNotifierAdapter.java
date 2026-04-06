package com.codefactory.appstripe.transactions.infrastructure.adapter;

import com.codefactory.appstripe.transactions.application.port.IMerchantNotifierPort;
import com.codefactory.appstripe.transactions.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class MerchantNotifierAdapter implements IMerchantNotifierPort {

    @Override
    public void notifyProcessingStart(Transaction transaction) {
        System.out.println("El procesamiento del pago con ID " + transaction.getId() + " ha comenzado.");
    }
}
