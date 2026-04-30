package com.codefactory.appstripe.identity.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
// builder para las pruebas
@Builder
public class ApiCredential {
    private String id;
    private String publicId; // se envia en los headers (ej: pk_live_...)
    private String secretHash; // Lse guarda en la bd, se compara con el hash del secret enviado por el cliente
    private String plainSecret; // para mostrar una vez al usuario
    private String merchantId;
    private boolean active; // si el merchant esta inactivo, se inactivan sus credenciales
    private ApiCredentialPermission permission; // permisos asociados a esta credencial

    public void markAsCreated(String publicId, String secretHash, String plainSecret) {
        this.publicId = publicId;
        this.secretHash = secretHash;
        this.plainSecret = plainSecret;
        this.active = true;
        this.permission = ApiCredentialPermission.READ_ONLY; // permiso por defecto
    }
}