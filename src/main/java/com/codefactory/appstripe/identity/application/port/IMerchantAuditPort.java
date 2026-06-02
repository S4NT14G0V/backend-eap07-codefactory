package com.codefactory.appstripe.identity.application.port;

import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;

import java.util.List;

/**
 * Puerto de salida para persistir y consultar los eventos de decisión
 * administrativa sobre comercios (bitácora inmutable).
 */
public interface IMerchantAuditPort {

    /**
     * Persiste un evento de auditoría generado por una acción admin.
     *
     * @param event el evento a guardar
     */
    void publish(MerchantAuditEvent event);

    /**
     * Recupera el historial completo de decisiones administrativas de un comercio,
     * ordenado cronológicamente (más reciente primero).
     *
     * @param merchantId ID del comercio
     * @return lista de eventos auditados
     */
    List<MerchantAuditEvent> findByMerchantId(String merchantId);
}
