package com.codefactory.appstripe.security.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TwoFactorSecretResponse {
    String secret;
}