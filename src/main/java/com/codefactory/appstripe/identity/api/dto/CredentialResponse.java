package com.codefactory.appstripe.identity.api.dto;

import com.codefactory.appstripe.identity.domain.ApiCredential;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CredentialResponse {
    String publicId;
    String secret;

    public static CredentialResponse fromDomain(ApiCredential credential) {
        return CredentialResponse.builder()
                .publicId(credential.getPublicId())
                .secret(credential.getPlainSecret())
                .build();
    }
}
