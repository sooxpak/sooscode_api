package com.sooscode.sooscode_api.application.auth.service;

import com.sooscode.sooscode_api.application.auth.dto.TokenResponse;

public interface GoogleAuthService {
    /**
     * Google OAuth 로그인 URL 생성
     */
    String buildGoogleLoginUrl();

    /**
     * Google Callback 처리 (회원 생성 or 기존 유저 조회 + 토큰 발급)
     */
    TokenResponse processGoogleCallback(String code);
}
