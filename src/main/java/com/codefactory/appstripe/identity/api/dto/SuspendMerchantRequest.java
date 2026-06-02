package com.codefactory.appstripe.identity.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Payload requerido para suspender un comercio.
 */
@Data
public class SuspendMerchantRequest {

    @NotBlank(message = "El motivo de la suspensión es obligatorio")
    private String reason;
}
