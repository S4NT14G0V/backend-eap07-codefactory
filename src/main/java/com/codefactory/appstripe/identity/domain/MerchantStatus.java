package com.codefactory.appstripe.identity.domain;

public enum MerchantStatus {
    /** Registro incompleto o no iniciado. */
    INACTIVE,

    /** Estado inicial tras completar el registro: en espera de revisión del administrador. */
    PENDING_VERIFICATION,

    /** Comercio aprobado por el administrador: puede operar en la plataforma. */
    ACTIVE,

    /** Mantenido por compatibilidad con registros anteriores. No usar en flujos nuevos. */
    VERIFIED,

    /** Comercio suspendido por el administrador: credenciales inhabilitadas. */
    SUSPENDED
}
