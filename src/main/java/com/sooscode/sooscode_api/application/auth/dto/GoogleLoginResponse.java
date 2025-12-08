package com.sooscode.sooscode_api.application.auth.dto;

public record GoogleLoginResponse(
        String accessToken,
        String refreshToken,
        LoginResponse user
) {}