package com.codefactory.appstripe.identity.domain;

/**
 * Acciones administrativas que pueden registrarse en la bitácora de un comercio.
 */
public enum MerchantAuditAction {
    /** El administrador aprobó el comercio; pasa a estado ACTIVE. */
    APPROVED,

    /** El administrador suspendió el comercio; credenciales inhabilitadas. */
    SUSPENDED
}
