package com.codefactory.appstripe.transactions.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una solicitud de exportación de transacciones.
 * Rastrea el ciclo de vida completo: desde la solicitud hasta la descarga del archivo.
 */
public class ExportRequest {

    private String id;                    // Identificador único de la solicitud
    private String merchantId;            // Comercio que solicitó la exportación
    private ExportStatus status;          // Estado actual de la solicitud
    private LocalDate periodFrom;         // Inicio del período a exportar
    private LocalDate periodTo;           // Fin del período a exportar
    private String filePath;              // Ruta del archivo generado en disco
    private String downloadToken;         // Token único para la descarga segura
    private LocalDateTime tokenExpiresAt; // Fecha de expiración del token de descarga
    private LocalDateTime createdAt;      // Fecha de creación de la solicitud
    private LocalDateTime completedAt;    // Fecha en que se completó la generación
    private long totalRecords;            // Total de registros exportados

    /**
     * Constructor para crear una NUEVA solicitud de exportación.
     * El estado inicial es PENDING automáticamente.
     */
    public ExportRequest(String merchantId, LocalDate periodFrom, LocalDate periodTo) {
        this.merchantId = merchantId;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.status = ExportStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor completo para reconstruir desde base de datos.
     */
    public ExportRequest(String id, String merchantId, ExportStatus status,
                         LocalDate periodFrom, LocalDate periodTo,
                         String filePath, String downloadToken,
                         LocalDateTime tokenExpiresAt, LocalDateTime createdAt,
                         LocalDateTime completedAt, long totalRecords) {
        this.id = id;
        this.merchantId = merchantId;
        this.status = status;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.filePath = filePath;
        this.downloadToken = downloadToken;
        this.tokenExpiresAt = tokenExpiresAt;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.totalRecords = totalRecords;
    }

    /** Marca la solicitud como en procesamiento */
    public void startProcessing() {
        this.status = ExportStatus.PROCESSING;
    }

    /** Marca la solicitud como completada exitosamente */
    public void markCompleted(String filePath, String downloadToken,
                              LocalDateTime tokenExpiresAt, long totalRecords) {
        this.status = ExportStatus.COMPLETED;
        this.filePath = filePath;
        this.downloadToken = downloadToken;
        this.tokenExpiresAt = tokenExpiresAt;
        this.completedAt = LocalDateTime.now();
        this.totalRecords = totalRecords;
    }

    /** Marca la solicitud como fallida */
    public void markFailed() {
        this.status = ExportStatus.FAILED;
        this.completedAt = LocalDateTime.now();
    }

    /** Verifica si el token de descarga aún es válido */
    public boolean isDownloadTokenValid() {
        return this.status == ExportStatus.COMPLETED
                && this.downloadToken != null
                && this.tokenExpiresAt != null
                && LocalDateTime.now().isBefore(this.tokenExpiresAt);
    }

    // Getters
    public String getId() { return id; }
    public String getMerchantId() { return merchantId; }
    public ExportStatus getStatus() { return status; }
    public LocalDate getPeriodFrom() { return periodFrom; }
    public LocalDate getPeriodTo() { return periodTo; }
    public String getFilePath() { return filePath; }
    public String getDownloadToken() { return downloadToken; }
    public LocalDateTime getTokenExpiresAt() { return tokenExpiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public long getTotalRecords() { return totalRecords; }
}
