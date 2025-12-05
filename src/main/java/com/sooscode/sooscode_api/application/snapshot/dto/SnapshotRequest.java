package com.sooscode.sooscode_api.application.snapshot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter

public class SnapshotRequest {
    private Long userId;
    private Long classId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;

}
