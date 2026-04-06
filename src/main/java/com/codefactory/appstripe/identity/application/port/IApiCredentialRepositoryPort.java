package com.codefactory.appstripe.identity.application.port;

import com.codefactory.appstripe.identity.domain.ApiCredential;

import java.util.List;

public interface IApiCredentialRepositoryPort {
    ApiCredential save(ApiCredential credential);
    List<ApiCredential> findByMerchantIdAndActiveTrue(String merchantId);
    long countByMerchantIdAndActiveTrue(String merchantId);
}