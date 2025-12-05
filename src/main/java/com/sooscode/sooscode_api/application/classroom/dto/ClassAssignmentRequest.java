package com.sooscode.sooscode_api.application.classroom.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClassAssignmentRequest {
    private Long classAssignmentId;
    private Long userId;
    private Long classId;
    private LocalDateTime createdAt;
}