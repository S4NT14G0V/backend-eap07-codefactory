package com.codefactory.appstripe.transactions.infrastructure.persistence.mapper;

import com.codefactory.appstripe.transactions.domain.ExportRequest;
import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.ExportRequestJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public interface ExportRequestMapper {

    ExportRequestJpaEntity toEntity(ExportRequest domain);

    ExportRequest toDomain(ExportRequestJpaEntity entity);

    /**
     * Factory method para MapStruct: reconstruye el objeto de dominio
     * usando el constructor completo, evitando ambigüedad de constructores.
     */
    @ObjectFactory
    default ExportRequest createExportRequest(ExportRequestJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ExportRequest(
                entity.getId(),
                entity.getMerchantId(),
                entity.getStatus(),
                entity.getPeriodFrom(),
                entity.getPeriodTo(),
                entity.getFilePath(),
                entity.getDownloadToken(),
                entity.getTokenExpiresAt(),
                entity.getCreatedAt(),
                entity.getCompletedAt(),
                entity.getTotalRecords()
        );
    }
}
