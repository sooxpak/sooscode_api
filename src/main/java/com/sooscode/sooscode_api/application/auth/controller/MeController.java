package com.sooscode.sooscode_api.application.auth.controller;

import com.sooscode.sooscode_api.application.auth.dto.MeResponse;
import com.sooscode.sooscode_api.application.auth.dto.TokenResponse;
import com.sooscode.sooscode_api.application.auth.service.AuthServiceImpl;
import com.sooscode.sooscode_api.application.auth.util.CookieUtil;
import com.sooscode.sooscode_api.domain.user.entity.User;
import com.sooscode.sooscode_api.global.api.response.ApiResponse;
import com.sooscode.sooscode_api.global.api.status.AuthStatus;
import com.sooscode.sooscode_api.global.api.status.GlobalStatus;
import com.sooscode.sooscode_api.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController  {
    private final AuthServiceImpl authService;

    /**
     * 현재 로그인 된 유저 정보
     * SecurityContext에 있는 CustomUserDetails 추출됨
     */
    @GetMapping
    public ResponseEntity<ApiResponse<MeResponse>> me(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        MeResponse meResponse = new MeResponse(
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getProfileImage()
        );
        return ApiResponse.ok(AuthStatus.ME_SUCCESS, meResponse);
    }

}
