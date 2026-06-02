package com.codefactory.appstripe.transactions.domain;

/**
 * Estados posibles de una solicitud de exportación de transacciones.
 * Sigue el ciclo de vida: PENDING → PROCESSING → COMPLETED | FAILED
 */
public enum ExportStatus {
    PENDING,     // Solicitud recibida, esperando procesamiento
    PROCESSING,  // Generación del archivo en curso
    COMPLETED,   // Archivo generado exitosamente, listo para descarga
    FAILED       // Error durante la generación del archivo
}
