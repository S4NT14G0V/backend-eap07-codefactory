package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommerceApplicationServiceTest {

    @Mock
    private ICommerceRepositoryPort commerceRepository;

    @InjectMocks
    private CommerceApplicationService commerceApplicationService;

    @Test
    @DisplayName("Debe registrar comercio VERIFIED cuando datos son válidos")
    void shouldRegisterMerchantSuccessfully() {
        when(commerceRepository.existsByBusinessId("900123456")).thenReturn(false);
        when(commerceRepository.existsByEmail("ops@merchant.com")).thenReturn(false);
        when(commerceRepository.save(any(Merchant.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
}
