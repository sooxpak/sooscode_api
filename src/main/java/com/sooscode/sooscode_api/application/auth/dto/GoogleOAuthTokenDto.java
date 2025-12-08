package com.sooscode.sooscode_api.application.auth.dto;

public record GoogleOAuthTokenDto(
        String accessToken,
        String expiresIn,
        String scope,
        String tokenType,
        String idToken
) {}
