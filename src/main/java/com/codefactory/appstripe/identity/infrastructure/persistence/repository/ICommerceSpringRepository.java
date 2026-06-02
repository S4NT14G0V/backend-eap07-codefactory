package com.codefactory.appstripe.identity.infrastructure.persistence.repository;

import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.identity.infrastructure.persistence.entity.MerchantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code merchants}.
 */
@Repository
public interface ICommerceSpringRepository extends JpaRepository<MerchantJpaEntity, String> {

    boolean existsByBusinessId(String businessId);

    boolean existsByEmail(String email);

    /** Filtra comercios por estado (ej: PENDING_VERIFICATION para el panel admin). */
    List<MerchantJpaEntity> findByStatus(MerchantStatus status);
}
