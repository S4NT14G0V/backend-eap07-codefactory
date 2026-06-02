package com.codefactory.appstripe.transactions.application.port;

import java.time.LocalDateTime;

/**
 * Puerto de salida para notificar al comercio cuando una exportación asíncrona
 * ha sido completada. La implementación puede enviar un correo, SMS, webhook, etc.
 */
public interface IExportNotifierPort {

    /**
     * Notifica al comercio que su archivo de exportación está listo para descargar.
     *
     * @param merchantId   identificador del comercio
     * @param downloadUrl  URL completa para descargar el archivo
     * @param expiresAt    fecha y hora de expiración del enlace de descarga
     */
    void notifyExportReady(String merchantId, String downloadUrl, LocalDateTime expiresAt);
}
