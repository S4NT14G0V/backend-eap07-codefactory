package com.codefactory.appstripe.identity.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.security.application.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommerceApplicationService {

    private final ICommerceRepositoryPort commerceRepository;
    private final AuthenticationService authenticationService;

    public Merchant registerMerchant(String businessName, String businessId, String email, String businessType) {
        if (commerceRepository.existsByBusinessId(businessId)) {
            throw new IllegalStateException("Ya existe un comercio con el número de identificación fiscal indicado");
        }
        if (commerceRepository.existsByEmail(email)) {
            throw new IllegalStateException("Ya existe un comercio con el correo electrónico indicado");
        }

        Merchant merchant = Merchant.builder()
                .id("mch_" + UUID.randomUUID().toString().replace("-", ""))
                .businessName(businessName)
                .businessId(businessId)
                .email(email)
                .businessType(businessType)
                .status(MerchantStatus.VERIFIED)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        Merchant savedMerchant = commerceRepository.save(merchant);

        // Crear usuario para el comercio
        authenticationService.createMerchantUser(email, savedMerchant.getId());

        // Retornamos el comercio
        return savedMerchant;
    }

    public Merchant getMerchantProfile(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }

        return commerceRepository.findById(merchantId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Comercio no encontrado"));
    }
}