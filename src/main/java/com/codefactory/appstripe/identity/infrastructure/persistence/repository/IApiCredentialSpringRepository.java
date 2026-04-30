package com.codefactory.appstripe.identity.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codefactory.appstripe.identity.infrastructure.persistence.entity.ApiCredentialJpaEntity;

@Repository
public interface IApiCredentialSpringRepository extends JpaRepository<ApiCredentialJpaEntity, String> {
    List<ApiCredentialJpaEntity> findByMerchantIdAndActiveTrue(String merchantId);
    long countByMerchantIdAndActiveTrue(String merchantId);
    Optional<ApiCredentialJpaEntity> findByPublicId(String publicId);
}
