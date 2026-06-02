package com.codefactory.appstripe.transactions.application.port;

import com.codefactory.appstripe.transactions.domain.ExportRequest;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia de solicitudes de exportación.
 * Define el contrato entre la capa de aplicación y la infraestructura.
 */
public interface IExportRequestRepositoryPort {

    /** Guarda o actualiza una solicitud de exportación */
    ExportRequest save(ExportRequest request);

    /** Busca una solicitud por su identificador */
    Optional<ExportRequest> findById(String id);

    /** Busca una solicitud por su token de descarga único */
    Optional<ExportRequest> findByDownloadToken(String token);
}
