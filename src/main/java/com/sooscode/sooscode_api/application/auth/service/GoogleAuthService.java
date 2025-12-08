package com.sooscode.sooscode_api.application.auth.service;

import com.sooscode.sooscode_api.application.auth.dto.GoogleOAuthTokenDto;
import com.sooscode.sooscode_api.application.auth.dto.GoogleUserDto;

public interface GoogleAuthService {

    /**
     * Google 로그인 URL 생성
     */
    String buildGoogleLoginUrl();

    /**
     * Authorization Code → Access Token
     */
    GoogleOAuthTokenDto getAccessToken(String code);

    /**
     * Access Token → Google 사용자 정보 조회
     */
    GoogleUserDto getUserInfo(String accessToken);
}
