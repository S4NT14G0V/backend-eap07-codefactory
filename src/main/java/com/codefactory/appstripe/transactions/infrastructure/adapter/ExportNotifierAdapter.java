package com.codefactory.appstripe.transactions.infrastructure.adapter;

import com.codefactory.appstripe.transactions.application.port.IExportNotifierPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementación mock del notificador de exportación.
 * En producción se reemplazaría con un envío real de correo (Spring Mail, SendGrid, etc.).
 * Consistente con el patrón de MerchantNotifierAdapter que usa System.out.println().
 */
@Component
public class ExportNotifierAdapter implements IExportNotifierPort {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void notifyExportReady(String merchantId, String downloadUrl, LocalDateTime expiresAt) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("📧 NOTIFICACIÓN DE EXPORTACIÓN (MOCK - correo simulado)");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("  Para:        Comercio " + merchantId);
        System.out.println("  Asunto:      Su archivo de transacciones está listo");
        System.out.println("  Enlace:      " + downloadUrl);
        System.out.println("  Expira el:   " + expiresAt.format(FORMATTER));
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
