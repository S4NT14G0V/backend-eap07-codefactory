package com.codefactory.appstripe.identity.api;

import com.codefactory.appstripe.identity.api.dto.MerchantAuditEventResponse;
import com.codefactory.appstripe.identity.api.dto.MerchantResponse;
import com.codefactory.appstripe.identity.api.dto.RegisterMerchantRequest;
import com.codefactory.appstripe.identity.api.dto.SuspendMerchantRequest;
import com.codefactory.appstripe.identity.application.CommerceApplicationService;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/merchants")
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

    @GetMapping
    public ResponseEntity<List<MerchantResponse>> listMerchants(
            @RequestParam(required = false) String status) {
        
        List<Merchant> merchants;
        if ("PENDING_VERIFICATION".equalsIgnoreCase(status)) {
            merchants = commerceApplicationService.listPendingMerchants();
        } else {
            merchants = commerceApplicationService.listAllMerchants();
        }

        List<MerchantResponse> response = merchants.stream()
                .map(MerchantResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{merchantId}/approve")
    public ResponseEntity<MerchantResponse> approveMerchant(
            @PathVariable String merchantId,
            Authentication authentication) {

        String adminEmail = authentication.getName(); // El username es el email
        Merchant approved = commerceApplicationService.approveMerchant(merchantId, adminEmail);
        return ResponseEntity.ok(MerchantResponse.fromDomain(approved));
    }

    @PatchMapping("/{merchantId}/suspend")
    public ResponseEntity<MerchantResponse> suspendMerchant(
            @PathVariable String merchantId,
            @Valid @RequestBody SuspendMerchantRequest request,
            Authentication authentication) {

        String adminEmail = authentication.getName();
        Merchant suspended = commerceApplicationService.suspendMerchant(merchantId, adminEmail, request.getReason());
        return ResponseEntity.ok(MerchantResponse.fromDomain(suspended));
    }

    @GetMapping("/{merchantId}/audit-log")
    public ResponseEntity<List<MerchantAuditEventResponse>> getAuditLog(@PathVariable String merchantId) {
        List<MerchantAuditEvent> events = commerceApplicationService.getMerchantAuditLog(merchantId);
        List<MerchantAuditEventResponse> response = events.stream()
                .map(MerchantAuditEventResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }
}
