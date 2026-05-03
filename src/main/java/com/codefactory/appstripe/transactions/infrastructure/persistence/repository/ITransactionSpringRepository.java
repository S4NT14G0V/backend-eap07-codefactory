package com.codefactory.appstripe.transactions.infrastructure.persistence.repository;

import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionSpringRepository extends JpaRepository<TransactionJpaEntity, String> {
    List<TransactionJpaEntity> findByMerchantId(String merchantId);
}
