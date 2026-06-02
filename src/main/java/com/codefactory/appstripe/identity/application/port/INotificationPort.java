package com.codefactory.appstripe.identity.application.port;

/**
 * Puerto de salida para el envío de notificaciones por correo electrónico
 * a los comercios. La implementación concreta es un mock de consola.
 */
public interface INotificationPort {

    /**
     * Notifica al comercio que su cuenta ha sido aprobada y activada.
     *
     * @param toEmail      correo del comercio
     * @param businessName nombre del negocio para personalizar el mensaje
     */
    void sendMerchantApprovalEmail(String toEmail, String businessName);

    /**
     * Notifica al comercio que su cuenta ha sido suspendida.
     *
     * @param toEmail      correo del comercio
     * @param businessName nombre del negocio
     * @param reason       motivo de la suspensión
     */
    void sendMerchantSuspensionEmail(String toEmail, String businessName, String reason);
}
