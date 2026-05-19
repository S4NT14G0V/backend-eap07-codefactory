package com.codefactory.appstripe.security.api.dto;

public record CsrfTokenResponse(
        String headerName,
        String parameterName,
        String token
) {
}
