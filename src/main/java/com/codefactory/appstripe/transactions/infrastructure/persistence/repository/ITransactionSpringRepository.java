package com.codefactory.appstripe.transactions.infrastructure.persistence.repository;

import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITransactionSpringRepository extends JpaRepository<TransactionJpaEntity, String> {

    List<TransactionJpaEntity> findByMerchantId(String merchantId);

    @Query("""
            SELECT t.status AS status, COUNT(t.id) AS total
            FROM TransactionJpaEntity t
            WHERE t.merchantId = :merchantId
              AND t.status IN :statuses
              AND t.createdAt >= :fromInclusive
              AND t.createdAt < :toExclusive
            GROUP BY t.status
            """)
    List<StatusCountProjection> countByStatusForDashboard(
            @Param("merchantId") String merchantId,
            @Param("statuses") List<TransactionStatus> statuses,
            @Param("fromInclusive") LocalDateTime fromInclusive,
            @Param("toExclusive") LocalDateTime toExclusive
    );

    interface StatusCountProjection {
        TransactionStatus getStatus();
        Long getTotal();
    }
}
