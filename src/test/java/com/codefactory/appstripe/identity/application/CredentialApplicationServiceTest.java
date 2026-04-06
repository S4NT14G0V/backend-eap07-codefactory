package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialApplicationServiceTest {

    @Mock private IApiCredentialRepositoryPort credentialRepository;
    @Mock private ICommerceRepositoryPort commerceRepository;
    @Mock private IApiKeyGeneratorPort keyGenerator;

    @InjectMocks private CredentialApplicationService service;

    // escenraio 1: generación exitosa
    @Test
    @DisplayName("Criterio 1: Generación exitosa de credenciales")
    void shouldGenerateCredentialsSuccessfully() {
        // Arrange
        String mchId = "MCH-001";
        Merchant merchant = Merchant.builder().id(mchId).status(MerchantStatus.VERIFIED).build();

        when(commerceRepository.findById(mchId)).thenReturn(Optional.of(merchant));
        when(credentialRepository.countByMerchantIdAndActiveTrue(mchId)).thenReturn(0L);
        when(keyGenerator.generatePublicId()).thenReturn("pk_live_123");
        when(keyGenerator.generateSecretKey()).thenReturn("sk_live_456");
        when(keyGenerator.hashSecret(anyString())).thenReturn("hashed_secret");

        // Act
        ApiCredential result = service.generateCredentials(mchId);

        // Assert
        assertNotNull(result.getPublicId(), "Debe mostrar el ID público");
        assertNotNull(result.getPlainSecret(), "Debe mostrar la clave secreta (solo esta vez)");
        verify(credentialRepository, times(1)).save(any());
    }

    // escenarios 2: comercio no verificado
    @Test
    @DisplayName("Criterio 2: Error si el comercio no está verificado")
    void shouldThrowExceptionWhenMerchantNotVerified() {
        // Arrange
        String mchId = "MCH-001";
        Merchant merchant = Merchant.builder().id(mchId).status(MerchantStatus.INACTIVE).build();

        when(commerceRepository.findById(mchId)).thenReturn(Optional.of(merchant));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.generateCredentials(mchId);
        });

        assertTrue(exception.getMessage().contains("activo y verificado"));
    }

    // escenario 3: limite alcanzado
    @Test
    @DisplayName("Criterio 3: Error cuando se alcanza el límite de 3 credenciales")
    void shouldThrowExceptionWhenLimitReached() {
        // Arrange
        String mchId = "MCH-001";
        Merchant merchant = Merchant.builder().id(mchId).status(MerchantStatus.VERIFIED).build();

        when(commerceRepository.findById(mchId)).thenReturn(Optional.of(merchant));
        when(credentialRepository.countByMerchantIdAndActiveTrue(mchId)).thenReturn(3L);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            service.generateCredentials(mchId);
        });
    }
}