package com.codefactory.appstripe.identity.api.dto;

import com.codefactory.appstripe.identity.domain.Merchant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MerchantResponse {
    String id;
    String businessName;
    String businessId;
    String email;
    String businessType;
    String status;
    String permission;

    public static MerchantResponse fromDomain(Merchant merchant) {
        return MerchantResponse.builder()
                .id(merchant.getId())
                .businessName(merchant.getBusinessName())
                .businessId(merchant.getBusinessId())
                .email(merchant.getEmail())
                .businessType(merchant.getBusinessType())
                .status(merchant.getStatus().name())
                .permission(merchant.getPermission().name())
                .build();
    }
}