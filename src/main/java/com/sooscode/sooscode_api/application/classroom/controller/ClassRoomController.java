package com.sooscode.sooscode_api.application.classroom.controller;

import com.sooscode.sooscode_api.application.classroom.dto.ClassRoomDetailResponse;
import com.sooscode.sooscode_api.application.classroom.service.ClassRoomService;
import com.sooscode.sooscode_api.global.api.response.ApiResponse;
import com.sooscode.sooscode_api.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classroom")
public class ClassRoomController {
    private final ClassRoomService classRoomService;

    /**
     * 클래스 접근 권한 확인
     * @param classId
     * @param userDetails
     * @return classRoomDetail
     */
    @GetMapping("/{classId}")
    public ResponseEntity<ApiResponse<ClassRoomDetailResponse>> getClassRoom(
            @PathVariable Long classId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ClassRoomDetailResponse classRoomDetail = classRoomService
                .getClassRoomDetail(classId, userDetails.getUser().getUserId());

        return ApiResponse.ok(classRoomDetail);
    }
}