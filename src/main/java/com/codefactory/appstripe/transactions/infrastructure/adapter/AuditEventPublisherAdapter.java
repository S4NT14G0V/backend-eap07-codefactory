package com.codefactory.appstripe.transactions.infrastructure.adapter;

import com.codefactory.appstripe.transactions.application.port.IAuditPublisherPort;
import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import com.codefactory.appstripe.transactions.infrastructure.persistence.mapper.TransactionMapper;
import com.codefactory.appstripe.transactions.infrastructure.persistence.repository.ITransactionSpringRepository;
import org.springframework.stereotype.Component;

@Component
public class AuditEventPublisherAdapter implements IAuditPublisherPort{



    @Override
    public void publishStatusChange(String transactionId, TransactionStatus oldStatus, TransactionStatus newStatus){
        System.out.println("Log de Auditoria: Transacción " + transactionId + " cambió de estado de " + oldStatus + " a " + newStatus);
    }

}
