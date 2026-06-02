package com.codefactory.appstripe.identity.api.dto;

import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * DTO para enviar la respuesta de un evento de auditoría de comercio.
 */
@Value
@Builder
public class MerchantAuditEventResponse {
    String id;
    String merchantId;
    String adminEmail;
    String action;
    String reason;
    Instant occurredAt;

    public static MerchantAuditEventResponse fromDomain(MerchantAuditEvent event) {
        return MerchantAuditEventResponse.builder()
                .id(event.getId())
                .merchantId(event.getMerchantId())
                .adminEmail(event.getAdminEmail())
                .action(event.getAction().name())
                .reason(event.getReason())
                .occurredAt(event.getOccurredAt())
                .build();
    }
}
