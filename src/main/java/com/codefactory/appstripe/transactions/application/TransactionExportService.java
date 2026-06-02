package com.codefactory.appstripe.transactions.application;

import com.codefactory.appstripe.transactions.application.port.IExportNotifierPort;
import com.codefactory.appstripe.transactions.application.port.IExportRequestRepositoryPort;
import com.codefactory.appstripe.transactions.application.port.ITransactionRepositoryPort;
import com.codefactory.appstripe.transactions.domain.ExportRequest;
import com.codefactory.appstripe.transactions.domain.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación para la exportación de transacciones.
 * Implementa la lógica dual:
 * - Síncrona (count <= umbral): genera CSV en memoria y retorna bytes
 * - Asíncrona (count > umbral): crea ExportRequest, genera en background, notifica por correo
 */
@Service
public class TransactionExportService {

    private final ITransactionRepositoryPort transactionRepositoryPort;
    private final IExportRequestRepositoryPort exportRequestRepositoryPort;
    private final IExportNotifierPort exportNotifierPort;

    @Value("${export.sync-threshold:1000}")
    private int syncThreshold;

    @Value("${export.storage-dir:./exports}")
    private String storageDir;

    @Value("${export.download-token-expiry-hours:24}")
    private int tokenExpiryHours;

    @Value("${export.base-download-url:http://localhost:8080/api/v1/transactions/export/download}")
    private String baseDownloadUrl;

    public TransactionExportService(
            ITransactionRepositoryPort transactionRepositoryPort,
            IExportRequestRepositoryPort exportRequestRepositoryPort,
            IExportNotifierPort exportNotifierPort
    ) {
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.exportRequestRepositoryPort = exportRequestRepositoryPort;
        this.exportNotifierPort = exportNotifierPort;
    }

    /**
     * Resultado de una exportación que puede ser síncrona (bytes listos) o asíncrona (request pendiente).
     */
    public record ExportResult(
            boolean synchronous,
            byte[] csvBytes,
            ExportRequest exportRequest
    ) {
        public static ExportResult sync(byte[] csvBytes) {
            return new ExportResult(true, csvBytes, null);
        }

        public static ExportResult async(ExportRequest request) {
            return new ExportResult(false, null, request);
        }
    }

    /**
     * Punto de entrada principal: decide si la exportación es síncrona o asíncrona
     * basándose en el conteo de transacciones vs. el umbral configurable.
     */
    public ExportResult exportTransactions(String merchantId, LocalDate from, LocalDate to) {
        // 1. Validaciones
        validateExportParams(merchantId, from, to);

        LocalDateTime fromInclusive = from.atStartOfDay();
        LocalDateTime toExclusive = to.plusDays(1).atStartOfDay();

        // 2. Contar transacciones del período
        long count = transactionRepositoryPort.countByMerchantIdAndCreatedAtBetween(
                merchantId, fromInclusive, toExclusive
        );

        if (count == 0) {
            throw new IllegalArgumentException(
                    "No se encontraron transacciones en el período " + from + " a " + to
            );
        }

        // 3. Decidir flujo síncrono o asíncrono
        if (count <= syncThreshold) {
            return handleSynchronousExport(merchantId, fromInclusive, toExclusive);
        } else {
            return handleAsynchronousExport(merchantId, from, to);
        }
    }

    /**
     * Exportación síncrona: genera el CSV en memoria y retorna los bytes.
     */
    private ExportResult handleSynchronousExport(String merchantId,
                                                  LocalDateTime fromInclusive,
                                                  LocalDateTime toExclusive) {
        List<Transaction> transactions = transactionRepositoryPort
                .findByMerchantIdAndCreatedAtBetween(merchantId, fromInclusive, toExclusive);

        try {
            byte[] csvBytes = TransactionCsvGenerator.generateCsvBytes(transactions);
            return ExportResult.sync(csvBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo CSV: " + e.getMessage(), e);
        }
    }

    /**
     * Exportación asíncrona: crea un ExportRequest y dispara la generación en background.
     */
    private ExportResult handleAsynchronousExport(String merchantId, LocalDate from, LocalDate to) {
        ExportRequest exportRequest = new ExportRequest(merchantId, from, to);
        ExportRequest saved = exportRequestRepositoryPort.save(exportRequest);

        // Disparar generación asíncrona
        generateExportAsync(saved.getId());

        return ExportResult.async(saved);
    }

    /**
     * Generación asíncrona del archivo CSV.
     * Se ejecuta en un hilo separado gracias a @Async.
     */
    @Async
    public void generateExportAsync(String exportRequestId) {
        ExportRequest exportRequest = exportRequestRepositoryPort.findById(exportRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Solicitud de exportación no encontrada con ID: " + exportRequestId));

        try {
            // 1. Marcar como en procesamiento
            exportRequest.startProcessing();
            exportRequestRepositoryPort.save(exportRequest);

            // 2. Obtener transacciones del período
            LocalDateTime fromInclusive = exportRequest.getPeriodFrom().atStartOfDay();
            LocalDateTime toExclusive = exportRequest.getPeriodTo().plusDays(1).atStartOfDay();

            List<Transaction> transactions = transactionRepositoryPort
                    .findByMerchantIdAndCreatedAtBetween(
                            exportRequest.getMerchantId(), fromInclusive, toExclusive
                    );

            // 3. Generar CSV y escribir a disco
            byte[] csvBytes = TransactionCsvGenerator.generateCsvBytes(transactions);
            String fileName = "export_" + exportRequest.getMerchantId() + "_"
                    + exportRequest.getPeriodFrom() + "_" + exportRequest.getPeriodTo() + ".csv";
            Path exportDir = Paths.get(storageDir);
            Files.createDirectories(exportDir);
            Path filePath = exportDir.resolve(fileName);
            Files.write(filePath, csvBytes);

            // 4. Generar token de descarga seguro
            String downloadToken = UUID.randomUUID().toString();
            LocalDateTime tokenExpiresAt = LocalDateTime.now().plusHours(tokenExpiryHours);

            // 5. Marcar como completado
            exportRequest.markCompleted(
                    filePath.toString(), downloadToken, tokenExpiresAt, transactions.size()
            );
            exportRequestRepositoryPort.save(exportRequest);

            // 6. Notificar al comercio
            String downloadUrl = baseDownloadUrl + "?token=" + downloadToken;
            exportNotifierPort.notifyExportReady(
                    exportRequest.getMerchantId(), downloadUrl, tokenExpiresAt
            );

        } catch (Exception e) {
            exportRequest.markFailed();
            exportRequestRepositoryPort.save(exportRequest);
            // Log del error (en producción se usaría SLF4J)
            System.err.println("Error generando exportación " + exportRequestId + ": " + e.getMessage());
        }
    }

    /**
     * Consulta el estado de una solicitud de exportación.
     */
    public ExportRequest getExportRequestStatus(String exportRequestId) {
        return exportRequestRepositoryPort.findById(exportRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Solicitud de exportación no encontrada con ID: " + exportRequestId));
    }

    /**
     * Descarga el archivo de una exportación completada usando el token de descarga.
     * Valida que el token sea válido y no haya expirado.
     */
    public byte[] downloadExport(String downloadToken) {
        ExportRequest exportRequest = exportRequestRepositoryPort.findByDownloadToken(downloadToken)
                .orElseThrow(() -> new RuntimeException(
                        "Enlace de descarga inválido o no encontrado"));

        if (!exportRequest.isDownloadTokenValid()) {
            throw new IllegalStateException(
                    "El enlace de descarga ha expirado. Solicite una nueva exportación.");
        }

        try {
            Path filePath = Paths.get(exportRequest.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de exportación: " + e.getMessage(), e);
        }
    }

    /**
     * Validaciones comunes de los parámetros de exportación.
     */
    private void validateExportParams(String merchantId, LocalDate from, LocalDate to) {
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Las fechas 'from' y 'to' son obligatorias");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final");
        }
    }
}
