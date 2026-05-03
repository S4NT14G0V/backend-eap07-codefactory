package com.codefactory.appstripe.identity.api;

import com.codefactory.appstripe.identity.api.dto.CredentialResponse;
import com.codefactory.appstripe.identity.api.dto.GenerateCredentialRequest;
import com.codefactory.appstripe.identity.application.CredentialApplicationService;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/credentials")
public class CredentialController {

    private final CredentialApplicationService credentialApplicationService;

    public CredentialController(CredentialApplicationService credentialApplicationService) {
        this.credentialApplicationService = credentialApplicationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<CredentialResponse> generate(@Valid @RequestBody GenerateCredentialRequest request) {
        ApiCredential generated = credentialApplicationService.generateCredentials(request.getMerchantId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CredentialResponse.fromDomain(generated));
    }
}
