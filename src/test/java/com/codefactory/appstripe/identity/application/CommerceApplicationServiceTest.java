package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IMerchantAuditPort;
import com.codefactory.appstripe.identity.application.port.INotificationPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantAuditAction;
import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.security.application.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommerceApplicationServiceTest {

    @Mock
    private ICommerceRepositoryPort commerceRepository;

    @Mock
    private IApiCredentialRepositoryPort credentialRepository;

    @Mock
    private IMerchantAuditPort merchantAuditPort;

    @Mock
    private INotificationPort notificationPort;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CommerceApplicationService service;

    private Merchant pendingMerchant;
    private Merchant activeMerchant;
    private Merchant suspendedMerchant;

    @BeforeEach
    void setUp() {
        pendingMerchant = Merchant.builder()
                .id("mch_123")
                .businessName("Test Store")
                .email("test@store.com")
                .status(MerchantStatus.PENDING_VERIFICATION)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        activeMerchant = Merchant.builder()
                .id("mch_123")
                .businessName("Test Store")
                .email("test@store.com")
                .status(MerchantStatus.ACTIVE)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        suspendedMerchant = Merchant.builder()
                .id("mch_123")
                .businessName("Test Store")
                .email("test@store.com")
                .status(MerchantStatus.SUSPENDED)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();
    }

    @Test
    void registerMerchant_shouldSetStatusToPendingVerification() {
        // Arrange
        when(commerceRepository.existsByBusinessId(anyString())).thenReturn(false);
        when(commerceRepository.existsByEmail(anyString())).thenReturn(false);
        when(commerceRepository.save(any(Merchant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Merchant result = service.registerMerchant("Name", "123", "a@a.com", "Retail");

        // Assert
        assertEquals(MerchantStatus.PENDING_VERIFICATION, result.getStatus());
        verify(commerceRepository).save(any(Merchant.class));
        verify(authenticationService).createMerchantUser("a@a.com", result.getId());
    }

    @Test
    void approveMerchant_whenPending_shouldSetActiveAndAudit() {
        // Arrange
        when(commerceRepository.findById("mch_123")).thenReturn(Optional.of(pendingMerchant));
        when(commerceRepository.save(any(Merchant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Merchant result = service.approveMerchant("mch_123", "admin@app.com");

        // Assert
        assertEquals(MerchantStatus.ACTIVE, result.getStatus());
        
        verify(commerceRepository).save(any(Merchant.class));
        verify(notificationPort).sendMerchantApprovalEmail("test@store.com", "Test Store");

        ArgumentCaptor<MerchantAuditEvent> auditCaptor = ArgumentCaptor.forClass(MerchantAuditEvent.class);
        verify(merchantAuditPort).publish(auditCaptor.capture());
        
        MerchantAuditEvent event = auditCaptor.getValue();
        assertEquals("mch_123", event.getMerchantId());
        assertEquals("admin@app.com", event.getAdminEmail());
        assertEquals(MerchantAuditAction.APPROVED, event.getAction());
        assertNull(event.getReason());
    }

    @Test
    void approveMerchant_whenNotPending_shouldThrowException() {
        // Arrange
        when(commerceRepository.findById("mch_123")).thenReturn(Optional.of(activeMerchant));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            service.approveMerchant("mch_123", "admin@app.com")
        );
        
        assertTrue(exception.getMessage().contains("Solo los comercios en estado PENDING_VERIFICATION pueden ser aprobados"));
        verify(commerceRepository, never()).save(any());
        verify(merchantAuditPort, never()).publish(any());
    }

    @Test
    void suspendMerchant_whenActive_shouldRevokeCredentialsAndAudit() {
        // Arrange
        when(commerceRepository.findById("mch_123")).thenReturn(Optional.of(activeMerchant));
        
        ApiCredential cred1 = ApiCredential.builder().id("cred1").active(true).build();
        ApiCredential cred2 = ApiCredential.builder().id("cred2").active(true).build();
        when(credentialRepository.findByMerchantIdAndActiveTrue("mch_123")).thenReturn(List.of(cred1, cred2));
        
        when(commerceRepository.save(any(Merchant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Merchant result = service.suspendMerchant("mch_123", "admin@app.com", "Actividad irregular");

        // Assert
        assertEquals(MerchantStatus.SUSPENDED, result.getStatus());
        
        // Verifica que las credenciales se desactivaron y guardaron
        assertFalse(cred1.isActive());
        assertFalse(cred2.isActive());
        verify(credentialRepository, times(2)).save(any(ApiCredential.class));
        
        verify(commerceRepository).save(any(Merchant.class));
        verify(notificationPort).sendMerchantSuspensionEmail("test@store.com", "Test Store", "Actividad irregular");

        ArgumentCaptor<MerchantAuditEvent> auditCaptor = ArgumentCaptor.forClass(MerchantAuditEvent.class);
        verify(merchantAuditPort).publish(auditCaptor.capture());
        
        MerchantAuditEvent event = auditCaptor.getValue();
        assertEquals("mch_123", event.getMerchantId());
        assertEquals("admin@app.com", event.getAdminEmail());
        assertEquals(MerchantAuditAction.SUSPENDED, event.getAction());
        assertEquals("Actividad irregular", event.getReason());
    }

    @Test
    void suspendMerchant_whenAlreadySuspended_shouldThrowException() {
        // Arrange
        when(commerceRepository.findById("mch_123")).thenReturn(Optional.of(suspendedMerchant));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            service.suspendMerchant("mch_123", "admin@app.com", "Fraude")
        );
        
        assertEquals("El comercio ya se encuentra suspendido", exception.getMessage());
        verify(credentialRepository, never()).findByMerchantIdAndActiveTrue(anyString());
        verify(commerceRepository, never()).save(any());
    }

    @Test
    void listPendingMerchants_shouldReturnOnlyPending() {
        // Arrange
        when(commerceRepository.findByStatus(MerchantStatus.PENDING_VERIFICATION)).thenReturn(List.of(pendingMerchant));

        // Act
        List<Merchant> result = service.listPendingMerchants();

        // Assert
        assertEquals(1, result.size());
        assertEquals(MerchantStatus.PENDING_VERIFICATION, result.get(0).getStatus());
        verify(commerceRepository).findByStatus(MerchantStatus.PENDING_VERIFICATION);
    }
}
