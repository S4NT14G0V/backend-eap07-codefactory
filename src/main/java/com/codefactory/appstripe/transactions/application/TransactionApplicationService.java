package com.codefactory.appstripe.transactions.application;

import com.codefactory.appstripe.transactions.application.port.IAuditPublisherPort;
import com.codefactory.appstripe.transactions.application.port.IMerchantNotifierPort;
import com.codefactory.appstripe.transactions.application.port.ITransactionRepositoryPort;
import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionApplicationService {
    private final ITransactionRepositoryPort transactionRepositoryPort;
    private final IAuditPublisherPort auditPublisherPort;
    private final IMerchantNotifierPort merchantNotifierPort;

    public TransactionApplicationService(ITransactionRepositoryPort transactionRepositoryPort,
                                         IAuditPublisherPort auditPublisherPort,
                                         IMerchantNotifierPort merchantNotifierPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.auditPublisherPort = auditPublisherPort;
        this.merchantNotifierPort = merchantNotifierPort;
    }

    public Transaction assignInitialStatusCreated(Transaction transaction){
        // El estado inicial es CREATED con anterioridad
        return transactionRepositoryPort.save(transaction);
    }

    public void startProcessing(String transactionId){
        Transaction transaction = transactionRepositoryPort.findById(transactionId).orElseThrow(()->new RuntimeException("Transacción no encontrada con ID: " + transactionId));

        // guardar el estado anterior u old
        TransactionStatus oldStatus = transaction.getStatus();

        //procesar el pago
        transaction.startProcessing();

        // guardar en base de datos
        transactionRepositoryPort.save(transaction);

        // guardar en auditoria
        auditPublisherPort.publishStatusChange(transaction.getId(), oldStatus, transaction.getStatus());

        // notificar procesamiento al comercio
        merchantNotifierPort.notifyProcessingStart(transaction);
    }

}
