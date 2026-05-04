package com.codefactory.appstripe.security.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.appstripe.security.api.dto.ActivateAccountRequest;
import com.codefactory.appstripe.security.api.dto.JwtResponse;
import com.codefactory.appstripe.security.api.dto.LoginRequest;
import com.codefactory.appstripe.security.application.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authenticationService.login(request.getEmail(), request.getPassword(), request.getTotpCode());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/merchant/activate")
    public ResponseEntity<JwtResponse> activateMerchant(@Valid @RequestBody ActivateAccountRequest request) {
        JwtResponse response = authenticationService.activateAccount(request.getInvitationToken(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }
}