package com.sooscode.sooscode_api.application.auth.dto;

public record GoogleOAuthToken(
        String accessToken,
        String expiresIn,
        String scope,
        String tokenType,
        String idToken
) {}
