package com.codefactory.appstripe.security.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.appstripe.security.api.dto.TwoFactorSecretResponse;
import com.codefactory.appstripe.security.api.dto.TwoFactorVerificationResponse;
import com.codefactory.appstripe.security.aplication.TwoFactorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/2fa")
public class AuthController {

    private final TwoFactorService twoFactorService;

    public AuthController(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    @PostMapping("/secret")
    public ResponseEntity<TwoFactorSecretResponse> generateSecret() {
        return ResponseEntity.ok(TwoFactorSecretResponse.builder()
                .secret(twoFactorService.generateSecret())
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<TwoFactorVerificationResponse> verify(@Valid @RequestBody CodeRequest req) {
        boolean valid = twoFactorService.verifyCode(req.getTwoFactorSecret(), req.getCode());
        return ResponseEntity.ok(TwoFactorVerificationResponse.builder()
                .valid(valid)
                .build());
    }
}