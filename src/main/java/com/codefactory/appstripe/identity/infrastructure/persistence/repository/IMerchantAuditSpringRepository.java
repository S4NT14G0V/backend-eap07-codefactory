package com.codefactory.appstripe.identity.infrastructure.persistence.repository;

import com.codefactory.appstripe.identity.infrastructure.persistence.entity.MerchantAuditEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code merchant_audit_events}.
 * Solo expone operaciones de INSERT y SELECT (sin UPDATE ni DELETE).
 */
@Repository
public interface IMerchantAuditSpringRepository
        extends JpaRepository<MerchantAuditEventJpaEntity, String> {

    /**
     * Recupera todos los eventos de auditoría de un comercio,
     * ordenados del más reciente al más antiguo.
     *
     * @param merchantId ID del comercio
     * @return lista ordenada de eventos
     */
    List<MerchantAuditEventJpaEntity> findByMerchantIdOrderByOccurredAtDesc(String merchantId);
}
