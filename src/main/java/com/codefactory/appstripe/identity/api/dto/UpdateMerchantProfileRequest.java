package com.codefactory.appstripe.identity.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMerchantProfileRequest {

    @NotBlank(message = "businessName es obligatorio")
    private String businessName;

    @NotBlank(message = "email es obligatorio")
    @Email(message = "email no tiene un formato válido")
    private String email;

    @NotBlank(message = "businessType es obligatorio")
    private String businessType;
}