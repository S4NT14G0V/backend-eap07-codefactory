package com.codefactory.appstripe.transactions.api;

import com.codefactory.appstripe.transactions.api.dto.ExportRequestResponse;
import com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest;
import com.codefactory.appstripe.transactions.application.TransactionExportService;
import com.codefactory.appstripe.transactions.domain.ExportRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para la exportación de transacciones.
 * Soporta dos flujos:
 * - Síncrono: descarga inmediata del CSV (200 OK)
 * - Asíncrono: acepta la solicitud y genera en background (202 Accepted)
 */
@RestController
@RequestMapping("/api/v1/transactions/export")
public class ExportController {

    private static final String BASE_URL = "/api/v1/transactions/export";

    private final TransactionExportService transactionExportService;

    public ExportController(TransactionExportService transactionExportService) {
        this.transactionExportService = transactionExportService;
    }

    /**
     * POST /api/v1/transactions/export
     * Solicita la exportación de transacciones para un período.
     * - Si el volumen es bajo: retorna CSV inmediato (200 OK)
     * - Si el volumen es alto: retorna 202 Accepted con detalles de la solicitud
     */
    @PostMapping
    public ResponseEntity<?> exportTransactions(
            Authentication authentication,
            @Valid @RequestBody ExportTransactionsRequest request) {

        String merchantId = extractMerchantId(authentication);

        TransactionExportService.ExportResult result =
                transactionExportService.exportTransactions(merchantId, request.getFrom(), request.getTo());

        if (result.synchronous()) {
            // Flujo síncrono: retornar CSV como descarga inmediata
            String fileName = "transacciones_" + request.getFrom() + "_" + request.getTo() + ".csv";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                    .body(result.csvBytes());
        } else {
            // Flujo asíncrono: retornar 202 Accepted con el estado de la solicitud
            ExportRequestResponse response = ExportRequestResponse.fromDomain(
                    result.exportRequest(), BASE_URL
            );

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
    }

    /**
     * GET /api/v1/transactions/export/{exportId}
     * Consulta el estado de una solicitud de exportación asíncrona.
     */
    @GetMapping("/{exportId}")
    public ResponseEntity<ExportRequestResponse> getExportStatus(@PathVariable String exportId) {
        ExportRequest exportRequest = transactionExportService.getExportRequestStatus(exportId);
        ExportRequestResponse response = ExportRequestResponse.fromDomain(exportRequest, BASE_URL);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/transactions/export/download?token={downloadToken}
     * Descarga el archivo CSV generado por una exportación asíncrona.
     * Valida que el token sea válido y no haya expirado.
     * - 200 OK: descarga exitosa del archivo
     * - 410 Gone: token expirado
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExport(@RequestParam String token) {
        byte[] csvBytes = transactionExportService.downloadExport(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transacciones_export.csv\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .body(csvBytes);
    }

    /**
     * Extrae el merchantId del token de autenticación.
     * Consistente con el patrón usado en TransactionController.
     */
    private String extractMerchantId(Authentication authentication) {
        if (authentication == null || authentication.getCredentials() == null) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }

        String merchantId = authentication.getCredentials().toString();
        if (merchantId.isBlank()) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }

        return merchantId;
    }
}
