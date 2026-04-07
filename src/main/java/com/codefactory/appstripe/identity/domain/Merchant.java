package com.codefactory.appstripe.identity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
    private String id;
    private String businessName;
    private String businessId; // NIT o RUT
    private String email;
    private String businessType;
    private MerchantStatus status; // Aquí usamos el Enum que creamos antes
}