package com.sooscode.sooscode_api.application.snapshot.controller;

import com.sooscode.sooscode_api.application.snapshot.dto.SnapshotSaveRequest;
import com.sooscode.sooscode_api.application.snapshot.service.SnapshotService;
import com.sooscode.sooscode_api.global.user.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequestMapping("/api/snapshot")
@Controller
public class SnapshotController {

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @PostMapping("/")
    public ResponseEntity<?> save(
            @RequestBody SnapshotSaveRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userKey = userDetails.getUser().getUserId();

        snapshotService.saveCodeSnapshot(dto, userKey);
        return ResponseEntity.ok("스냅샷 저장 완료");
    }

}
