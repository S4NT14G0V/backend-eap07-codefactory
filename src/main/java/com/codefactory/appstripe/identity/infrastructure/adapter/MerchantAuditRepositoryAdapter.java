package com.codefactory.appstripe.identity.infrastructure.adapter;

import com.codefactory.appstripe.identity.application.port.IMerchantAuditPort;
import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;
import com.codefactory.appstripe.identity.infrastructure.persistence.entity.MerchantAuditEventJpaEntity;
import com.codefactory.appstripe.identity.infrastructure.persistence.repository.IMerchantAuditSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptador de salida que conecta el puerto {@link IMerchantAuditPort}
 * con la base de datos MySQL vía Spring Data JPA.
 * Implementa el patrón Anti-Corruption Layer con mapeos explícitos
 * entre el objeto de dominio y la entidad JPA.
 */
@Component
@RequiredArgsConstructor
public class MerchantAuditRepositoryAdapter implements IMerchantAuditPort {

    private final IMerchantAuditSpringRepository springRepository;

    @Override
    public void publish(MerchantAuditEvent event) {
        springRepository.save(toEntity(event));
    }

    @Override
    public List<MerchantAuditEvent> findByMerchantId(String merchantId) {
        return springRepository
                .findByMerchantIdOrderByOccurredAtDesc(merchantId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // -------------------------------------------------------------------------
    // Mapeos dominio ↔ entidad JPA
    // -------------------------------------------------------------------------

    private MerchantAuditEventJpaEntity toEntity(MerchantAuditEvent event) {
        MerchantAuditEventJpaEntity entity = new MerchantAuditEventJpaEntity();
        entity.setId(event.getId());
        entity.setMerchantId(event.getMerchantId());
        entity.setAdminEmail(event.getAdminEmail());
        entity.setAction(event.getAction());
        entity.setReason(event.getReason());
        entity.setOccurredAt(event.getOccurredAt());
        return entity;
    }

    private MerchantAuditEvent toDomain(MerchantAuditEventJpaEntity entity) {
        return MerchantAuditEvent.builder()
                .id(entity.getId())
                .merchantId(entity.getMerchantId())
                .adminEmail(entity.getAdminEmail())
                .action(entity.getAction())
                .reason(entity.getReason())
                .occurredAt(entity.getOccurredAt())
                .build();
    }
}
