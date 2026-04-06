package com.codefactory.appstripe.security.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.codefactory.appstripe.security.aplication.TwoFactorService;

@RestController
public class AuthController {

    private final TwoFactorService twoFactorService;

    public AuthController(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verify(@RequestBody CodeRequest req) {
        boolean valid = twoFactorService.verifyCode(req.getUser(), req.getCode());
        return ResponseEntity.ok(valid);
    }
}