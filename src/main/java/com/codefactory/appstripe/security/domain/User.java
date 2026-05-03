package com.codefactory.appstripe.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String role; // "ADMIN" o "MERCHANT"
    private String merchantId;
    private String twoFactorSecret;
    private boolean twoFactorEnabled;
    private String invitationToken;
    private boolean accountActivated;
}
