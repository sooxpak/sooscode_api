package com.sooscode.sooscode_api.application.auth.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sooscode.sooscode_api.application.auth.dto.GoogleOAuthTokenDto;
import com.sooscode.sooscode_api.application.auth.dto.GoogleUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 필요한 3개만 @Value로 받음 */
    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    /** URL은 고정 사용 */
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";


    /**
     * 구글 로그인 URL 생성
     */
    @Override
    public String buildGoogleLoginUrl() {

        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        return AUTH_URL
                + "?client_id=" + clientId
                + "&redirect_uri=" + encodedRedirectUri
                + "&response_type=code"
                + "&scope=" + URLEncoder.encode("openid email profile", StandardCharsets.UTF_8)
                + "&access_type=offline"
                + "&prompt=consent";
    }


    /**
     * Authorization Code → Access Token 요청
     */
    @Override
    public GoogleOAuthTokenDto getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(TOKEN_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("[Google] Access Token 요청 실패: " + response.getStatusCode());
        }

        try {
            JsonNode json = objectMapper.readTree(response.getBody());

            return new GoogleOAuthTokenDto(
                    json.get("access_token").asText(),
                    json.get("expires_in").asText(),
                    json.get("scope").asText(),
                    json.get("token_type").asText(),
                    json.has("id_token") ? json.get("id_token").asText() : null
            );

        } catch (Exception e) {
            throw new RuntimeException("[Google] Token 응답 파싱 실패", e);
        }
    }


    /**
     * Access Token → Google 사용자 정보 조회
     */
    @Override
    public GoogleUserDto getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(USERINFO_URL, HttpMethod.GET, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("[Google] UserInfo 요청 실패: " + response.getStatusCode());
        }

        try {
            JsonNode json = objectMapper.readTree(response.getBody());

            return new GoogleUserDto(
                    json.get("email").asText(),
                    json.get("name").asText(),
                    json.get("picture").asText()
            );

        } catch (Exception e) {
            throw new RuntimeException("[Google] UserInfo 파싱 실패", e);
        }
    }
}
