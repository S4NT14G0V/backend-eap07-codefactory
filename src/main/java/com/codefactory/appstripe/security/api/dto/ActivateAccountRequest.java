package com.codefactory.appstripe.security.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivateAccountRequest {

    @NotBlank(message = "El token de invitación es obligatorio")
    private String invitationToken;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String newPassword;
}
