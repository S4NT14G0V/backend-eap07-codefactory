package com.codefactory.appstripe.transactions.infrastructure.persistence.mapper;


import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.TransactionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toDomain(TransactionJpaEntity entity);
    TransactionJpaEntity toEntity(Transaction transaction);
}
