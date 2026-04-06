package com.codefactory.appstripe.security;

import com.codefactory.appstripe.security.api.AuthController;
import com.codefactory.appstripe.security.api.CodeRequest;
import com.codefactory.appstripe.security.aplication.TwoFactorService;
import com.codefactory.appstripe.security.aplication.port.TwoFactorPort;
import com.codefactory.appstripe.security.domain.User;
import com.codefactory.appstripe.security.infrastructure.adapter.GoogleAuthAdapter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityTests {

    @Mock
    private TwoFactorPort twoFactorPort;

    @InjectMocks
    private TwoFactorService twoFactorService;

    @Mock
    private TwoFactorService mockedTwoFactorService;

    private AuthController authController;
    private final GoogleAuthAdapter googleAuthAdapter = new GoogleAuthAdapter();

    @BeforeEach
    void setup() {
        authController = new AuthController(mockedTwoFactorService);
    }

    @Test
    @DisplayName("Debería devolver true cuando el código 2FA es válido")
    void shouldReturnTrueWhenCodeIsValid() {
        User user = new User("merchant1", "password123", "secret-key");
        int code = 123456;

        when(twoFactorPort.verify(user.getTwoFactorSecret(), code)).thenReturn(true);

        boolean result = twoFactorService.verifyCode(user, code);

        assertTrue(result, "El servicio debe validar el código 2FA correcto");
        verify(twoFactorPort).verify(user.getTwoFactorSecret(), code);
    }

    @Test
    @DisplayName("Debería devolver false cuando el código 2FA es inválido")
    void shouldReturnFalseWhenCodeIsInvalid() {
        User user = new User("merchant1", "password123", "secret-key");
        int code = 654321;

        when(twoFactorPort.verify(user.getTwoFactorSecret(), code)).thenReturn(false);

        boolean result = twoFactorService.verifyCode(user, code);

        assertFalse(result, "El servicio debe rechazar el código 2FA incorrecto");
        verify(twoFactorPort).verify(user.getTwoFactorSecret(), code);
    }

    @Test
    @DisplayName("Debería generar un secreto 2FA no nulo y no vacío")
    void shouldGenerateNonEmptySecret() {
        String secret = googleAuthAdapter.generateSecret();

        assertNotNull(secret, "El secreto 2FA no debe ser nulo");
        assertFalse(secret.isBlank(), "El secreto 2FA no debe ser vacío");
    }

    @Test
    @DisplayName("Debería verificar un código válido generado por Google Authenticator")
    void shouldVerifyValidCode() {
        String secret = googleAuthAdapter.generateSecret();
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        int validCode = authenticator.getTotpPassword(secret);

        assertTrue(googleAuthAdapter.verify(secret, validCode), "El adaptador debe aceptar un código TOTP válido");
    }

    @Test
    @DisplayName("Debería rechazar un código 2FA inválido")
    void shouldRejectInvalidCode() {
        String secret = googleAuthAdapter.generateSecret();
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        int validCode = authenticator.getTotpPassword(secret);
        int invalidCode = validCode == 0 ? 1 : validCode + 1;

        assertFalse(googleAuthAdapter.verify(secret, invalidCode), "El adaptador debe rechazar un código TOTP inválido");
    }

    @Test
    @DisplayName("Debería devolver OK y true cuando el código 2FA es válido en el controlador")
    void shouldReturnOkWhenCodeIsValidInController() {
        CodeRequest request = new CodeRequest("merchant1", 123456);

        when(mockedTwoFactorService.verifyCode(any(User.class), eq(123456))).thenReturn(true);

        ResponseEntity<?> response = authController.verify(request);

        assertEquals(200, response.getStatusCodeValue(), "Debe retornar 200 OK");
        assertTrue((Boolean) response.getBody(), "El cuerpo de la respuesta debe ser true cuando el código es válido");
        verify(mockedTwoFactorService).verifyCode(any(User.class), eq(123456));
    }
}
