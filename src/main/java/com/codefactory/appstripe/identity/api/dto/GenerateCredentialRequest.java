package com.codefactory.appstripe.identity.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateCredentialRequest {

    @NotBlank(message = "merchantId es obligatorio")
    private String merchantId;
}
