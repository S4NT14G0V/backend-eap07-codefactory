package com.codefactory.appstripe.identity.infrastructure.persistence.repository;

import com.codefactory.appstripe.identity.infrastructure.persistence.entity.ApiCredentialJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IApiCredentialSpringRepository extends JpaRepository<ApiCredentialJpaEntity, String> {
    List<ApiCredentialJpaEntity> findByMerchantIdAndActiveTrue(String merchantId);

    long countByMerchantIdAndActiveTrue(String merchantId);
}
