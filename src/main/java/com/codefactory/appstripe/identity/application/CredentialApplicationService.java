package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CredentialApplicationService {

    private final IApiCredentialRepositoryPort credentialRepository;
    private final ICommerceRepositoryPort commerceRepository;
    private final IApiKeyGeneratorPort keyGenerator;

    @Transactional
    public ApiCredential generateCredentials(String merchantId) {
        // BUSCAR EL COMERCIO
        Merchant merchant = commerceRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado"));

        // ESCENARIO 2: Comercio no verificado
        if (merchant.getStatus() != MerchantStatus.VERIFIED) {
            throw new IllegalStateException("No es posible generar credenciales hasta que el comercio esté activo y verificado");
        }

        // ESCENARIO 3: Límite máximo (3 credenciales)
        long activeCount = credentialRepository.countByMerchantIdAndActiveTrue(merchantId);
        if (activeCount >= 3) {
            throw new IllegalStateException("Se ha alcanzado el límite permitido de credenciales activas");
        }

        // ESCENARIO 1: Generación exitosa
        String plainSecret = keyGenerator.generateSecretKey();
        String publicId = keyGenerator.generatePublicId();

        ApiCredential newCredential = ApiCredential.builder()
                .merchantId(merchantId)
                .publicId(publicId)
                .secretHash(keyGenerator.hashSecret(plainSecret)) // Guardamos el hash, no la clave real
                .active(true)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        credentialRepository.save(newCredential);

        // Retornamos una versión que SÍ incluye la clave plana para mostrarla UNA vez
        return ApiCredential.builder()
                .publicId(publicId)
                .plainSecret(plainSecret) // Este campo SOLO existe en este momento
                .build();
    }
    @Transactional
    public ApiCredential revokeCredential(String publicId) {
        ApiCredential credential = credentialRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NoSuchElementException("Credencial no encontrada: " + publicId));

        credential.revoke();

        return credentialRepository.save(credential);
    }
}
