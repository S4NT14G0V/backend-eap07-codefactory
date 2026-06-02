package com.codefactory.appstripe.transactions.infrastructure.adapter;

import com.codefactory.appstripe.transactions.application.port.IExportRequestRepositoryPort;
import com.codefactory.appstripe.transactions.domain.ExportRequest;
import com.codefactory.appstripe.transactions.infrastructure.persistence.entity.ExportRequestJpaEntity;
import com.codefactory.appstripe.transactions.infrastructure.persistence.mapper.ExportRequestMapper;
import com.codefactory.appstripe.transactions.infrastructure.persistence.repository.IExportRequestSpringRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter que conecta el puerto IExportRequestRepositoryPort con el repositorio Spring Data.
 * Traduce entre el dominio (ExportRequest) y la infraestructura (ExportRequestJpaEntity).
 */
@Component
public class ExportRequestRepositoryAdapter implements IExportRequestRepositoryPort {

    private final IExportRequestSpringRepository springRepository;
    private final ExportRequestMapper mapper;

    public ExportRequestRepositoryAdapter(IExportRequestSpringRepository springRepository,
                                          ExportRequestMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public ExportRequest save(ExportRequest request) {
        ExportRequestJpaEntity entity = mapper.toEntity(request);
        ExportRequestJpaEntity saved = springRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ExportRequest> findById(String id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ExportRequest> findByDownloadToken(String token) {
        return springRepository.findByDownloadToken(token).map(mapper::toDomain);
    }
}
