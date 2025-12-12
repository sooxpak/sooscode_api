package com.sooscode.sooscode_api.application.snapshot.dto;

import com.sooscode.sooscode_api.domain.snapshot.entity.CodeSnapshot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SnapShotResponse {

    private Long snapshotId;
    private Long userId;
    private Long classId;
    private String title;
    private String content;
    private SnapshotLanguage language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SnapShotResponse from(CodeSnapshot entity){
        return SnapShotResponse.builder()
                .snapshotId(entity.getCodeSnapshotId())
                .userId(entity.getUser().getUserId())
                .classId(entity.getClassRoom().getClassId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .language(entity.getLanguage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
