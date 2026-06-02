package com.codefactory.appstripe.identity.api.dto;

import lombok.Data;

@Data
public class UpdateMerchantProfileRequest {

    private String businessName;

    private String email;

    private String businessType;
}