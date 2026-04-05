package com.codefactory.appstripe.transactions.infrastructure.persistence.repository;

import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionSpringRepository extends JpaRepository<TransactionJpaEntity, String> {
    // vacia pero debe existir para crear la conexion con la base de datos
}
