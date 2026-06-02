package com.codefactory.appstripe.identity.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * Objeto de dominio que representa un evento de auditoría originado por
 * una decisión administrativa (aprobación o suspensión) sobre un comercio.
 * Es inmutable por diseño; se construye una única vez y se persiste.
 */
@Getter
@Builder
public class MerchantAuditEvent {

    /** Identificador único del evento (UUID). */
    private String id;

    /** ID del comercio afectado. */
    private String merchantId;

    /** E-mail del administrador que ejecutó la acción. */
    private String adminEmail;

    /** Tipo de acción realizada. */
    private MerchantAuditAction action;

    /**
     * Motivo de la decisión. Obligatorio en SUSPENDED; puede ser null en APPROVED.
     */
    private String reason;

    /** Momento exacto en que ocurrió el evento (UTC). */
    private Instant occurredAt;
}
