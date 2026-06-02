package com.codefactory.appstripe.transactions.infrastructure.persistence.repository;

import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.ExportRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExportRequestSpringRepository extends JpaRepository<ExportRequestJpaEntity, String> {

    /** Busca una solicitud de exportación por su token de descarga único */
    Optional<ExportRequestJpaEntity> findByDownloadToken(String downloadToken);

    /** Lista las solicitudes de un comercio, ordenadas de más reciente a más antigua */
    List<ExportRequestJpaEntity> findByMerchantIdOrderByCreatedAtDesc(String merchantId);
}
