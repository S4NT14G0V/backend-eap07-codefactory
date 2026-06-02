package com.codefactory.appstripe.identity.infrastructure.persistence.entity;

import com.codefactory.appstripe.identity.domain.MerchantAuditAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

/**
 * Entidad JPA que mapea la tabla {@code merchant_audit_events}.
 * Representa el registro inmutable de una decisión administrativa sobre un comercio.
 * La tabla nunca recibe UPDATEs ni DELETEs: solo INSERTs.
 */
@Data
@Entity
@Table(name = "merchant_audit_events")
public class MerchantAuditEventJpaEntity {

    /** UUID del evento de auditoría. */
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    /** ID del comercio afectado. Referencia lógica a la tabla merchants. */
    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    /** E-mail del administrador que ejecutó la acción. */
    @Column(name = "admin_email", nullable = false)
    private String adminEmail;

    /** Tipo de acción: APPROVED o SUSPENDED. */
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private MerchantAuditAction action;

    /**
     * Motivo de la decisión. Obligatorio en suspensiones; null en aprobaciones.
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    /** Timestamp exacto del evento en UTC. */
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;
}
