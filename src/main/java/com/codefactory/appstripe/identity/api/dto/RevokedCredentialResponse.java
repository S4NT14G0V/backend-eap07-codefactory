package com.codefactory.appstripe.identity.api.dto;

import com.codefactory.appstripe.identity.domain.ApiCredential;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RevokedCredentialResponse {
    String publicId;
    String merchantId;
    boolean active;
    String status;

    public static RevokedCredentialResponse fromDomain(ApiCredential credential) {
        return RevokedCredentialResponse.builder()
                .publicId(credential.getPublicId())
                .merchantId(credential.getMerchantId())
                .active(credential.isActive())
                .status(credential.isActive() ? "ACTIVE" : "REVOKED")
                .build();
    }
}