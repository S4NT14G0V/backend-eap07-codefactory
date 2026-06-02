package com.codefactory.appstripe.identity.application.port;

import java.time.Instant;

public interface ICredentialAuditPort {
    void publishCredentialRevoked(String publicId, String actorMerchantId, Instant revokedAt);
}