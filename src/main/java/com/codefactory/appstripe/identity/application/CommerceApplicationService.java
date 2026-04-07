package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommerceApplicationService {

    private final ICommerceRepositoryPort commerceRepository;

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
                .build();

        return commerceRepository.save(merchant);
    }
}
