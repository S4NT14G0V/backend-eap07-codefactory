package com.codefactory.appstripe.identity.api;

import com.codefactory.appstripe.identity.api.dto.MerchantResponse;
import com.codefactory.appstripe.identity.api.dto.RegisterMerchantRequest;
import com.codefactory.appstripe.identity.application.CommerceApplicationService;
import com.codefactory.appstripe.identity.domain.Merchant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchants")
public class CommerceController {

    private final CommerceApplicationService commerceApplicationService;

    public CommerceController(CommerceApplicationService commerceApplicationService) {
        this.commerceApplicationService = commerceApplicationService;
    }

    @PostMapping
    public ResponseEntity<MerchantResponse> register(@Valid @RequestBody RegisterMerchantRequest request) {
        Merchant merchant = commerceApplicationService.registerMerchant(
                request.getBusinessName(),
                request.getBusinessId(),
                request.getEmail(),
                request.getBusinessType());

        return ResponseEntity.status(HttpStatus.CREATED).body(MerchantResponse.fromDomain(merchant));
    }
}
