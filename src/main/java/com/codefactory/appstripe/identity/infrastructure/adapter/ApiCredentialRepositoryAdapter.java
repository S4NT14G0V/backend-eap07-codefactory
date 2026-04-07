package com.codefactory.appstripe.identity.infrastructure.adapter;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.infrastructure.persistence.entity.ApiCredentialJpaEntity;
import com.codefactory.appstripe.identity.infrastructure.persistence.repository.IApiCredentialSpringRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiCredentialRepositoryAdapter implements IApiCredentialRepositoryPort {

    private final IApiCredentialSpringRepository springRepository;

    public ApiCredentialRepositoryAdapter(IApiCredentialSpringRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public ApiCredential save(ApiCredential credential) {
        ApiCredentialJpaEntity saved = springRepository.save(toEntity(credential));
        return toDomain(saved);
    }

    @Override
    public List<ApiCredential> findByMerchantIdAndActiveTrue(String merchantId) {
        return springRepository.findByMerchantIdAndActiveTrue(merchantId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long countByMerchantIdAndActiveTrue(String merchantId) {
        return springRepository.countByMerchantIdAndActiveTrue(merchantId);
    }

    private ApiCredentialJpaEntity toEntity(ApiCredential credential) {
        ApiCredentialJpaEntity entity = new ApiCredentialJpaEntity();
        entity.setId(credential.getId());
        entity.setPublicId(credential.getPublicId());
        entity.setSecretHash(credential.getSecretHash());
        entity.setMerchantId(credential.getMerchantId());
        entity.setActive(credential.isActive());
        return entity;
    }

    private ApiCredential toDomain(ApiCredentialJpaEntity entity) {
        return ApiCredential.builder()
                .id(entity.getId())
                .publicId(entity.getPublicId())
                .secretHash(entity.getSecretHash())
                .merchantId(entity.getMerchantId())
                .active(entity.isActive())
                .build();
    }
}
