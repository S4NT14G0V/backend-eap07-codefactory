package com.codefactory.appstripe.transactions.infrastructure.persistence.mapper;


import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.TransactionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionJpaEntity toEntity(Transaction domain);

    Transaction toDomain(TransactionJpaEntity entity);

    // ESTA ES LA SOLUCIÓN MÁGICA:
    // Le decimos a MapStruct exactamente cómo construir el objeto de dominio
    // usando el constructor completo, evitando cualquier ambigüedad.
    @ObjectFactory
    default Transaction createTransaction(TransactionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Transaction(
                entity.getId(),
                entity.getMerchantId(),
                entity.getAmount(),
                entity.getStatus()
        );
    }
}