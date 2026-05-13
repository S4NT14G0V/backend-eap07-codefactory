package com.codefactory.appstripe.security.application;

import com.codefactory.appstripe.security.api.dto.JwtResponse;
import com.codefactory.appstripe.security.application.port.IJwtProviderPort;
import com.codefactory.appstripe.security.application.port.IPasswordEncoderPort;
import com.codefactory.appstripe.security.application.port.IUserRepositoryPort;
import com.codefactory.appstripe.security.application.port.TwoFactorPort;
import com.codefactory.appstripe.security.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private IUserRepositoryPort userRepository;

    @Mock
    private IPasswordEncoderPort passwordEncoder;

    @Mock
    private IJwtProviderPort jwtProvider;

    @Mock
    private TwoFactorPort twoFactorPort;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("CP-S1-014: inicia sesion con segundo factor valido")
    void shouldLoginWhenTwoFactorCodeIsValid() {
        // Arrange
        User user = User.builder()
                .email("admin@mitienda.com")
                .password("encoded-password")
                .role("MERCHANT")
                .merchantId("mch_123")
                .accountActivated(true)
                .twoFactorEnabled(true)
                .twoFactorSecret("totp-secret")
                .build();

        when(userRepository.findByEmail("admin@mitienda.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);
        when(twoFactorPort.verify("totp-secret", 123456)).thenReturn(true);
        when(jwtProvider.generateToken(user)).thenReturn("jwt-token");

        // Act
        JwtResponse response = authenticationService.login("admin@mitienda.com", "plain-password", 123456);

        // Assert
        assertEquals("jwt-token", response.getToken());
        assertEquals("MERCHANT", response.getRole());
        assertEquals("mch_123", response.getMerchantId());
    }

    @Test
    @DisplayName("CP-S1-016: rechaza inicio de sesion con segundo factor invalido")
    void shouldRejectLoginWhenTwoFactorCodeIsInvalid() {
        // Arrange
        User user = User.builder()
                .email("admin@mitienda.com")
                .password("encoded-password")
                .accountActivated(true)
                .twoFactorEnabled(true)
                .twoFactorSecret("totp-secret")
                .build();

        when(userRepository.findByEmail("admin@mitienda.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);
        when(twoFactorPort.verify("totp-secret", 111111)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> authenticationService.login("admin@mitienda.com", "plain-password", 111111));

        assertTrue(exception.getMessage().contains("2FA"));
        verify(jwtProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Debe rechazar login si la cuenta no ha sido activada")
    void shouldRejectLoginWhenAccountIsNotActivated() {
        // Arrange
        User user = User.builder()
                .email("admin@mitienda.com")
                .accountActivated(false)
                .build();

        when(userRepository.findByEmail("admin@mitienda.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> authenticationService.login("admin@mitienda.com", "plain-password", null));

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Debe activar cuenta de comercio con token de invitacion valido")
    void shouldActivateMerchantAccountWithValidInvitationToken() {
        // Arrange
        User user = User.builder()
                .email("admin@mitienda.com")
                .role("MERCHANT")
                .merchantId("mch_123")
                .invitationToken("invite-token")
                .accountActivated(false)
                .build();

        when(userRepository.findByInvitationToken("invite-token")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtProvider.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        JwtResponse response = authenticationService.activateAccount("invite-token", "new-password");

        // Assert
        assertEquals("jwt-token", response.getToken());
        assertEquals("MERCHANT", response.getRole());
        assertEquals("mch_123", response.getMerchantId());
        assertTrue(user.isAccountActivated());
        assertEquals("encoded-new-password", user.getPassword());
        assertNull(user.getInvitationToken());
    }

    @Test
    @DisplayName("Debe crear usuario de comercio en estado pendiente de activacion")
    void shouldCreateMerchantUserPendingActivation() {
        // Arrange
        when(userRepository.existsByEmail("admin@mitienda.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = authenticationService.createMerchantUser("admin@mitienda.com", "mch_123");

        // Assert
        assertEquals("admin@mitienda.com", result.getEmail());
        assertEquals("MERCHANT", result.getRole());
        assertEquals("mch_123", result.getMerchantId());
        assertNotNull(result.getInvitationToken());
        assertTrue(!result.isAccountActivated());
    }
}
