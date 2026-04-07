package com.codefactory.appstripe.identity.infrastructure.persistence.repository;

import com.codefactory.appstripe.identity.infrastructure.persistence.entity.MerchantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommerceSpringRepository extends JpaRepository<MerchantJpaEntity, String> {
    boolean existsByBusinessId(String businessId);

    boolean existsByEmail(String email);
}
