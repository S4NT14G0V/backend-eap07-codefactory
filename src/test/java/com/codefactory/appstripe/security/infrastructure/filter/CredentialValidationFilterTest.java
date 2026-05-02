package com.codefactory.appstripe.security.infrastructure.filter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class CredentialValidationFilterTest {

    @Mock
    private IApiCredentialRepositoryPort credentialRepository;

    @Mock
    private IApiKeyGeneratorPort keyGenerator;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CredentialValidationFilter filter;

    // ✅ Escenario 1: Credenciales válidas con permiso PAYMENTS
    @Test
    @DisplayName("Criterio 1: Debe permitir la solicitud con credenciales válidas y permiso PAYMENTS")
    void shouldAllowRequestWithValidCredentialsAndPaymentsPermission() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        request.addHeader("X-Public-Id", "pk_live_123");
        request.addHeader("X-Secret", "sk_live_abc");
        request.addHeader("X-Merchant-Id", "mch-001");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ApiCredential credential = ApiCredential.builder()
                .publicId("pk_live_123")
                .secretHash("hashed_secret")
                .merchantId("mch-001")
                .active(true)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        when(credentialRepository.findByPublicId("pk_live_123"))
                .thenReturn(Optional.of(credential));
        when(keyGenerator.hashSecret("sk_live_abc"))
                .thenReturn("hashed_secret");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());
    }

    // ❌ Escenario 2: Headers vacíos
    @Test
    @DisplayName("Criterio 2: Debe rechazar solicitud cuando faltan los headers de credenciales")
    void shouldRejectRequestWhenHeadersAreMissing() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        // No agregamos headers

        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(401, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    // ❌ Escenario 3: Credencial no existe o inactiva
    @Test
    @DisplayName("Criterio 3: Debe rechazar solicitud cuando la credencial no existe o está inactiva")
    void shouldRejectRequestWhenCredentialNotFound() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        request.addHeader("X-Public-Id", "pk_live_invalida");
        request.addHeader("X-Secret", "sk_live_abc");
        request.addHeader("X-Merchant-Id", "mch-001");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(credentialRepository.findByPublicId("pk_live_invalida"))
                .thenReturn(Optional.empty());

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(401, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    // ❌ Escenario 4: Secret incorrecto
    @Test
    @DisplayName("Criterio 4: Debe rechazar solicitud cuando el secret es incorrecto")
    void shouldRejectRequestWhenSecretIsWrong() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        request.addHeader("X-Public-Id", "pk_live_123");
        request.addHeader("X-Secret", "sk_live_incorrecta");
        request.addHeader("X-Merchant-Id", "mch-001");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ApiCredential credential = ApiCredential.builder()
                .publicId("pk_live_123")
                .secretHash("hashed_secret_correcto")
                .merchantId("mch-001")
                .active(true)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        when(credentialRepository.findByPublicId("pk_live_123"))
                .thenReturn(Optional.of(credential));
        when(keyGenerator.hashSecret("sk_live_incorrecta"))
                .thenReturn("hashed_secret_incorrecto"); // ← diferente al guardado

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(401, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    // ❌ Escenario 5: Credenciales de otro comercio
    @Test
    @DisplayName("Criterio 5: Debe rechazar solicitud cuando las credenciales pertenecen a otro comercio")
    void shouldRejectRequestWhenCredentialsBelongToAnotherMerchant() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        request.addHeader("X-Public-Id", "pk_live_123");
        request.addHeader("X-Secret", "sk_live_abc");
        request.addHeader("X-Merchant-Id", "mch-intruso"); // ← comercio diferente

        MockHttpServletResponse response = new MockHttpServletResponse();

        ApiCredential credential = ApiCredential.builder()
                .publicId("pk_live_123")
                .secretHash("hashed_secret")
                .merchantId("mch-001") // ← comercio real de la credencial
                .active(true)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        when(credentialRepository.findByPublicId("pk_live_123"))
                .thenReturn(Optional.of(credential));
        when(keyGenerator.hashSecret("sk_live_abc"))
                .thenReturn("hashed_secret");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(401, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    // ❌ Escenario 6: Sin permiso para pagos
    @Test
    @DisplayName("Criterio 6: Debe rechazar solicitud cuando las credenciales solo tienen permiso READ_ONLY")
    void shouldRejectRequestWhenCredentialsHaveInsufficientPermissions() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions");
        request.addHeader("X-Public-Id", "pk_live_123");
        request.addHeader("X-Secret", "sk_live_abc");
        request.addHeader("X-Merchant-Id", "mch-001");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ApiCredential credential = ApiCredential.builder()
                .publicId("pk_live_123")
                .secretHash("hashed_secret")
                .merchantId("mch-001")
                .active(true)
                .permission(ApiCredentialPermission.READ_ONLY) // ← solo lectura
                .build();

        when(credentialRepository.findByPublicId("pk_live_123"))
                .thenReturn(Optional.of(credential));
        when(keyGenerator.hashSecret("sk_live_abc"))
                .thenReturn("hashed_secret");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(401, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }
}