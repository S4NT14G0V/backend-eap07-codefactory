package com.codefactory.appstripe.transactions.api.dto;

import com.codefactory.appstripe.transactions.domain.ExportRequest;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DTO de respuesta para solicitudes de exportación.
 * Incluye links HATEOAS para navegación según el modelo de madurez de Richardson.
 */
@Value
@Builder
public class ExportRequestResponse {

    String exportId;
    String status;
    long totalRecords;
    String message;
    LocalDate periodFrom;
    LocalDate periodTo;
    LocalDateTime createdAt;
    LocalDateTime completedAt;
    Map<String, String> links;

    /**
     * Construye la respuesta a partir del dominio ExportRequest,
     * incluyendo links HATEOAS dinámicos según el estado.
     */
    public static ExportRequestResponse fromDomain(ExportRequest exportRequest, String baseUrl) {
        Map<String, String> hateoasLinks = new LinkedHashMap<>();

        // Link self siempre presente
        hateoasLinks.put("self", baseUrl + "/" + exportRequest.getId());

        // Link de descarga solo cuando está completado
        if (exportRequest.getDownloadToken() != null) {
            hateoasLinks.put("download", baseUrl + "/download?token=" + exportRequest.getDownloadToken());
        }

        // Mensaje descriptivo según el estado
        String message = switch (exportRequest.getStatus()) {
            case PENDING -> "Su solicitud de exportación ha sido recibida y está en cola.";
            case PROCESSING -> "El archivo se está generando. Recibirá una notificación por correo cuando esté listo.";
            case COMPLETED -> "El archivo está listo para descargar. Utilice el enlace proporcionado.";
            case FAILED -> "Ocurrió un error al generar el archivo. Intente nuevamente.";
        };

        return ExportRequestResponse.builder()
                .exportId(exportRequest.getId())
                .status(exportRequest.getStatus().name())
                .totalRecords(exportRequest.getTotalRecords())
                .message(message)
                .periodFrom(exportRequest.getPeriodFrom())
                .periodTo(exportRequest.getPeriodTo())
                .createdAt(exportRequest.getCreatedAt())
                .completedAt(exportRequest.getCompletedAt())
                .links(hateoasLinks)
                .build();
    }
}
