package com.codefactory.appstripe.identity.infrastructure.adapter;

import com.codefactory.appstripe.identity.application.port.INotificationPort;
import org.springframework.stereotype.Component;

/**
 * Adaptador de notificaciones — implementación MOCK para desarrollo y pruebas.
 *
 * <p>En producción, este adaptador se reemplaza por uno que integre un proveedor
 * SMTP real (SendGrid, AWS SES, etc.) sin modificar el dominio ni la aplicación,
 * gracias al patrón de puerto-adaptador (Hexagonal Architecture).</p>
 */
@Component
public class NotificationAdapter implements INotificationPort {

    private static final String LINE =
            "═══════════════════════════════════════════════════════════════";

    @Override
    public void sendMerchantApprovalEmail(String toEmail, String businessName) {
        System.out.println(LINE);
        System.out.println("📧  NOTIFICACIÓN DE ACTIVACIÓN DE COMERCIO  (MOCK – correo simulado)");
        System.out.println(LINE);
        System.out.println("  Para:         " + toEmail);
        System.out.println("  Comercio:     " + businessName);
        System.out.println("  Asunto:       ¡Tu cuenta ha sido activada en AppStripe!");
        System.out.println("  Mensaje:      Tu comercio ha sido revisado y aprobado por nuestro equipo.");
        System.out.println("                Ya puedes iniciar sesión y comenzar a operar en la plataforma.");
        System.out.println(LINE);
    }

    @Override
    public void sendMerchantSuspensionEmail(String toEmail, String businessName, String reason) {
        System.out.println(LINE);
        System.out.println("📧  NOTIFICACIÓN DE SUSPENSIÓN DE COMERCIO  (MOCK – correo simulado)");
        System.out.println(LINE);
        System.out.println("  Para:         " + toEmail);
        System.out.println("  Comercio:     " + businessName);
        System.out.println("  Asunto:       Tu cuenta ha sido suspendida en AppStripe");
        System.out.println("  Motivo:       " + reason);
        System.out.println("  Mensaje:      Todas tus credenciales han sido inhabilitadas de forma inmediata.");
        System.out.println("                Contacta a soporte si consideras que esto es un error.");
        System.out.println(LINE);
    }
}
