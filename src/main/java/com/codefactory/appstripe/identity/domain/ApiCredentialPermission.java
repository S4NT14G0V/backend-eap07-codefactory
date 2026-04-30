package com.codefactory.appstripe.identity.domain;

public enum ApiCredentialPermission {
    READ_ONLY,   // Solo puede consultar reportes
    PAYMENTS,    // Puede iniciar pagos
    FULL_ACCESS  // Acceso completo
}
