package com.sooscode.sooscode_api.application.classroom.controller;

import com.sooscode.sooscode_api.application.classroom.dto.ClassRoomCreateRequest;
import com.sooscode.sooscode_api.application.classroom.dto.ClassRoomResponse;
import com.sooscode.sooscode_api.application.classroom.dto.MyClassResponse;
import com.sooscode.sooscode_api.application.classroom.service.ClassRoomService;
import com.sooscode.sooscode_api.application.snapshot.service.SnapshotService;
import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.global.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
@Slf4j
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    
    // 추후 Service에 @Service 선언 후 사용 예정
    // private final SnapshotService snapshotService;

    @GetMapping("/{classId}/detail")
    public ResponseEntity<?> getClassDetail(@PathVariable Long classId) {
        log.info("getClassDetail 실행");

        ClassRoomResponse.Detail detail = classRoomService.getClassDetail(classId);

        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<?> createClassRoom(@RequestBody ClassRoomCreateRequest request) {
        log.info("createClassRoom Method");

        ClassRoom saved = classRoomService.createClassRoom(request);

        return ResponseEntity.ok(saved);
    }

    // 해당 강사가 담당하는 클래스 정보
    @GetMapping("/teacher/{userId}")
    public ResponseEntity<?> getClassesByTeacher(@PathVariable Long userId) {

        log.info("getClassesByTeacher Controller userId={}", userId);

        var response = classRoomService.getClassesByTeacher(userId);

        return ResponseEntity.ok(response);
    }

    // 내가 현재 가지고있는 강의 조회 ( 학생입장에서 제목,썸네일,담당강사 )
    @GetMapping("/me/classes")
    public ResponseEntity<?> getMyClasses(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = userDetails.getUser().getUserId();

        log.info("getMyClasses Controller userId={}, page={}, size={}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MyClassResponse> response = classRoomService.getMyClasses(userId, pageable);

        return ResponseEntity.ok(response);
    }



}
