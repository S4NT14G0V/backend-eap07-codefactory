package com.codefactory.appstripe.identity.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.appstripe.identity.api.dto.MerchantResponse;
import com.codefactory.appstripe.identity.api.dto.UpdateMerchantProfileRequest;
import com.codefactory.appstripe.identity.application.CommerceApplicationService;
import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.transactions.application.port.ITransactionRepositoryPort;
import com.codefactory.appstripe.transactions.domain.Transaction;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/merchant-portal")
public class MerchantPortalController {

    private final CommerceApplicationService commerceApplicationService;
    private final IApiCredentialRepositoryPort credentialRepository;
    private final ITransactionRepositoryPort transactionRepository;

    public MerchantPortalController(
            CommerceApplicationService commerceApplicationService,
            IApiCredentialRepositoryPort credentialRepository,
            ITransactionRepositoryPort transactionRepository
    ) {
        this.commerceApplicationService = commerceApplicationService;
        this.credentialRepository = credentialRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<MerchantResponse> getProfile(Authentication authentication) {
        String merchantId = extractMerchantId(authentication);
        Merchant merchant = commerceApplicationService.getMerchantProfile(merchantId);
        return ResponseEntity.ok(MerchantResponse.fromDomain(merchant));
    }

    @GetMapping("/credentials")
    public ResponseEntity<List<ApiCredential>> getCredentials(Authentication authentication) {
        String merchantId = extractMerchantId(authentication);
        List<ApiCredential> credentials = credentialRepository.findByMerchantIdAndActiveTrue(merchantId);
        return ResponseEntity.ok(credentials);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(Authentication authentication) {
        String merchantId = extractMerchantId(authentication);
        List<Transaction> transactions = transactionRepository.findByMerchantId(merchantId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<MerchantResponse> putProfile(Authentication authentication ,
        @Valid @RequestBody UpdateMerchantProfileRequest request) {
        
        String merchantId = extractMerchantId(authentication);
        Merchant merchant = commerceApplicationService.updateMerchant(
                merchantId,
            request.getBusinessName(),
            request.getEmail(),
            request.getBusinessType()
        );
        

        return ResponseEntity.ok(MerchantResponse.fromDomain(merchant));
    }

    private String extractMerchantId(Authentication authentication) {
        if (authentication == null || authentication.getCredentials() == null) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }

        String merchantId = authentication.getCredentials().toString();

        if (merchantId.isBlank()) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }

        return merchantId;
    }
}