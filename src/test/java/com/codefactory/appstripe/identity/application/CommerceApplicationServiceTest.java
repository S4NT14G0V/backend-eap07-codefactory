package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.security.application.AuthenticationService;
import com.codefactory.appstripe.security.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommerceApplicationServiceTest {

    @Mock
    private ICommerceRepositoryPort commerceRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CommerceApplicationService commerceApplicationService;

    @Test
    @DisplayName("Debe registrar comercio VERIFIED cuando datos son válidos")
    void shouldRegisterMerchantSuccessfully() {
        when(commerceRepository.existsByBusinessId("900123456")).thenReturn(false);
        when(commerceRepository.existsByEmail("ops@merchant.com")).thenReturn(false);
        when(commerceRepository.save(any(Merchant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(authenticationService.createMerchantUser(anyString(), anyString())).thenReturn(new User());

        Merchant result = commerceApplicationService.registerMerchant(
                "Tienda Demo",
                "900123456",
                "ops@merchant.com",
                "Retail");

        assertEquals("Tienda Demo", result.getBusinessName());
        assertEquals(MerchantStatus.VERIFIED, result.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar registro si email ya existe")
    void shouldFailWhenEmailExists() {
        when(commerceRepository.existsByBusinessId("900123456")).thenReturn(false);
        when(commerceRepository.existsByEmail("ops@merchant.com")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> commerceApplicationService.registerMerchant(
                "Tienda Demo",
                "900123456",
                "ops@merchant.com",
                "Retail"));
    }
    @Test
    @DisplayName("Debe consultar perfil de comercio existente")
    void shouldGetMerchantProfile() {
        Merchant merchant = Merchant.builder()
                .id("mch_123")
                .businessName("Tienda Demo")
                .businessId("900123456")
                .email("ops@merchant.com")
                .businessType("Retail")
                .status(MerchantStatus.VERIFIED)
                .build();

        when(commerceRepository.findById("mch_123")).thenReturn(java.util.Optional.of(merchant));

        Merchant result = commerceApplicationService.getMerchantProfile("mch_123");

        assertEquals("mch_123", result.getId());
        assertEquals("Tienda Demo", result.getBusinessName());
        assertEquals("ops@merchant.com", result.getEmail());
    }

    @Test
    @DisplayName("Debe fallar si el comercio no existe")
    void shouldFailWhenMerchantDoesNotExist() {
        when(commerceRepository.findById("mch_missing")).thenReturn(java.util.Optional.empty());

        assertThrows(java.util.NoSuchElementException.class,
                () -> commerceApplicationService.getMerchantProfile("mch_missing"));
    }

    @Test
    @DisplayName("Debe fallar si el merchantId viene vacío")
    void shouldFailWhenMerchantIdIsBlank() {
        assertThrows(IllegalStateException.class,
                () -> commerceApplicationService.getMerchantProfile(" "));
    }
}
