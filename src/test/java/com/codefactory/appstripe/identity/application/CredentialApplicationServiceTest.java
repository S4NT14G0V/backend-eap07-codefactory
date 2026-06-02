package com.codefactory.appstripe.identity.application;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.application.port.ICredentialAuditPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.identity.domain.exception.CredentialAccessDeniedException;

@ExtendWith(MockitoExtension.class)
class CredentialApplicationServiceTest {

    @Mock private IApiCredentialRepositoryPort credentialRepository;
    @Mock private ICommerceRepositoryPort commerceRepository;
    @Mock private IApiKeyGeneratorPort keyGenerator;
    @Mock private ICredentialAuditPort credentialAudit;

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
    @Test
    @DisplayName("HU-10: revoca una credencial activa exitosamente")
    void shouldRevokeCredentialSuccessfully() {
        // Arrange
        String publicId = "pk_live_123";

        ApiCredential activeCredential = ApiCredential.builder()
                .id("cred-001")
                .publicId(publicId)
                .secretHash("hashed_secret")
                .merchantId("mch_001")
                .active(true)
                .build();

        when(credentialRepository.findByPublicId(publicId))
                .thenReturn(Optional.of(activeCredential));

        when(credentialRepository.save(any(ApiCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ApiCredential result = service.revokeCredential(publicId, "mch_001");

        // Assert
        assertFalse(result.isActive());
        assertEquals(publicId, result.getPublicId());
        verify(credentialRepository).findByPublicId(publicId);
        verify(credentialRepository).save(any(ApiCredential.class));
    }

    @Test
    @DisplayName("HU-10: retorna error si la credencial no existe")
    void shouldThrowExceptionWhenCredentialDoesNotExist() {
        // Arrange
        String publicId = "pk_live_missing";

        when(credentialRepository.findByPublicId(publicId))
                .thenReturn(Optional.empty());

                // Act & Assert
                assertThrows(NoSuchElementException.class, () -> {
                        service.revokeCredential(publicId, "mch_001");
                });

        verify(credentialRepository).findByPublicId(publicId);
        verify(credentialRepository, never()).save(any(ApiCredential.class));
    }
    @Test
@DisplayName("HU009 - Escenario 1: Revocación exitosa con auditoría registrada")
void shouldRevokeCredential_AndRegisterAuditEvent() {
    // Arrange
    String publicId = "pk_live_123";
    String merchantId = "mch_propietario";

    ApiCredential activeCredential = ApiCredential.builder()
            .id("cred-001")
            .publicId(publicId)
            .secretHash("hashed_secret")
            .merchantId(merchantId)
            .active(true)
            .build();

    when(credentialRepository.findByPublicId(publicId))
            .thenReturn(Optional.of(activeCredential));
    when(credentialRepository.save(any(ApiCredential.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    ApiCredential result = service.revokeCredential(publicId, merchantId);

    // Assert
    assertFalse(result.isActive());
    assertEquals(publicId, result.getPublicId());
    verify(credentialAudit, times(1))
            .publishCredentialRevoked(eq(publicId), eq(merchantId), any());
}

@Test
@DisplayName("HU009 - Escenario 2: Credencial revocada queda active=false para que el filtro la rechace")
void shouldSetActiveToFalse_SoFilterRejectsSubsequentRequests() {
    // Arrange
    String publicId = "pk_live_abc";
    String merchantId = "mch_001";

    ApiCredential activeCredential = ApiCredential.builder()
            .publicId(publicId)
            .merchantId(merchantId)
            .active(true)
            .build();

    when(credentialRepository.findByPublicId(publicId))
            .thenReturn(Optional.of(activeCredential));
    when(credentialRepository.save(any(ApiCredential.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    ApiCredential revoked = service.revokeCredential(publicId, merchantId);

    // Assert
    assertFalse(revoked.isActive(),
            "active=false garantiza que CredentialValidationFilter rechace futuros intentos");
}

@Test
@DisplayName("HU009 - Escenario 3: No es posible revocar una credencial ya revocada")
void shouldThrowException_WhenCredentialAlreadyRevoked() {
    // Arrange
    String publicId = "pk_live_ya_revocada";
    String merchantId = "mch_001";

    ApiCredential alreadyRevoked = ApiCredential.builder()
            .publicId(publicId)
            .merchantId(merchantId)
            .active(false)
            .build();

    when(credentialRepository.findByPublicId(publicId))
            .thenReturn(Optional.of(alreadyRevoked));

    // Act & Assert
    IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> service.revokeCredential(publicId, merchantId));

    assertTrue(ex.getMessage().contains("ya fue revocada"));
    verify(credentialRepository, never()).save(any());
    verify(credentialAudit, never()).publishCredentialRevoked(any(), any(), any());
}

@Test
@DisplayName("HU009 - Escenario 4: No es posible revocar una credencial de otro comercio")
void shouldThrowException_WhenRevokingCredentialFromAnotherMerchant() {
    // Arrange
    String publicId = "pk_live_otro_comercio";
    String propietario = "mch_propietario";
    String intruso = "mch_intruso";

    ApiCredential credential = ApiCredential.builder()
            .publicId(publicId)
            .merchantId(propietario)
            .active(true)
            .build();

    when(credentialRepository.findByPublicId(publicId))
            .thenReturn(Optional.of(credential));

    // Act & Assert
    assertThrows(CredentialAccessDeniedException.class,
            () -> service.revokeCredential(publicId, intruso));

    verify(credentialRepository, never()).save(any());
    verify(credentialAudit, never()).publishCredentialRevoked(any(), any(), any());
}
}