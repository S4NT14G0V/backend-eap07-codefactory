package com.codefactory.appstripe.security.infrastructure.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CredentialValidationFilter extends OncePerRequestFilter {

    private final IApiCredentialRepositoryPort credentialRepository;
    private final IApiKeyGeneratorPort keyGenerator;

    public CredentialValidationFilter(IApiCredentialRepositoryPort credentialRepository,
                                      IApiKeyGeneratorPort keyGenerator) {
        this.credentialRepository = credentialRepository;
        this.keyGenerator = keyGenerator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/v1/transactions")) {
            filterChain.doFilter(request, response);
            return;
        }

        String publicId  = request.getHeader("X-Public-Id");
        String secret    = request.getHeader("X-Secret");
        String merchantId = request.getHeader("X-Merchant-Id");

        if (isBlank(publicId) || isBlank(secret) || isBlank(merchantId)) {
            sendUnauthorized(response, "MISSING_CREDENTIALS", "Credenciales requeridas en headers");
            return;
        }

        Optional<ApiCredential> credentialOpt = credentialRepository.findByPublicId(publicId);
        if (credentialOpt.isEmpty() || !credentialOpt.get().isActive()) {
            sendUnauthorized(response, "INVALID_CREDENTIALS", "Credenciales inválidas o inactivas");
            return;
        }

        ApiCredential credential = credentialOpt.get();

        String hashedSecret = keyGenerator.hashSecret(secret);
        if (!hashedSecret.equals(credential.getSecretHash())) {
            sendUnauthorized(response, "INVALID_CREDENTIALS", "Credenciales inválidas");
            return;
        }
        if (!credential.getMerchantId().equals(merchantId)) {
            sendUnauthorized(response, "CREDENTIAL_MISMATCH",
                    "Las credenciales no son válidas para este comercio");
            return;
        }

        if (credential.getPermission() != ApiCredentialPermission.PAYMENTS
                && credential.getPermission() != ApiCredentialPermission.FULL_ACCESS) {
            sendUnauthorized(response, "INSUFFICIENT_PERMISSIONS",
                    "Sus credenciales no tienen permiso para esta operación");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response,
                                   String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"errorCode\":\"" + errorCode + "\",\"message\":\"" + message + "\"}"
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}