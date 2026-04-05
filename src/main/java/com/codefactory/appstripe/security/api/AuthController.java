package com.codefactory.appstripe.security.api;

@RestController
public class AuthController {

    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verify(@RequestBody CodeRequest req) {
        boolean valid = twoFactorService.verifyCode(req.getUser(), req.getCode());
        return ResponseEntity.ok(valid);
    }
}