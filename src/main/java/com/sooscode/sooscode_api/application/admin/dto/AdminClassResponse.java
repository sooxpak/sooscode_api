package com.sooscode.sooscode_api.application.admin.dto;

import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.enums.ClassMode;
import com.sooscode.sooscode_api.domain.classroom.enums.ClassStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class AdminClassResponse {

    /**
     * 클래스 목록 아이템
     */
    @Data
    @Builder
    public static class ListItem {
        private Long classId;
        private String title;
        private boolean isOnline;
        private ClassStatus status;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private String instructorName;
        private Integer participantCount;
        private LocalDateTime createdAt;

        public static ListItem from(ClassRoom classRoom, String instructorName, Integer participantCount) {
            return ListItem.builder()
                    .classId(classRoom.getClassId())
                    .title(classRoom.getTitle())
                    .isOnline(classRoom.isOnline())
                    .status(classRoom.getStatus())
                    .startedAt(classRoom.getStartedAt())
                    .endedAt(classRoom.getEndedAt())
                    .instructorName(instructorName)
                    .participantCount(participantCount)
                    .createdAt(classRoom.getCreatedAt())
                    .build();
        }
    }

    /**
     * 클래스 상세 정보
     */
    @Data
    @Builder
    public static class Detail {
        private Long classId;
        private String title;
        private String description;
        private boolean isOnline;
        private boolean isActive;
        private ClassStatus status;
        private ClassMode mode;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private Long fileId;
        private String instructorName;
        private Integer participantCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Detail from(ClassRoom classRoom, String instructorName, Integer participantCount) {
            return Detail.builder()
                    .classId(classRoom.getClassId())
                    .title(classRoom.getTitle())
                    .description(classRoom.getDescription())
                    .isOnline(classRoom.isOnline())
                    .isActive(classRoom.isActive())
                    .status(classRoom.getStatus())
                    .mode(classRoom.getMode())
                    .startedAt(classRoom.getStartedAt())
                    .endedAt(classRoom.getEndedAt())
                    .fileId(classRoom.getFile() != null ? classRoom.getFile().getFileId() : null)
                    .instructorName(instructorName)
                    .participantCount(participantCount)
                    .createdAt(classRoom.getCreatedAt())
                    .updatedAt(classRoom.getUpdatedAt())
                    .build();
        }
    }

    /**
     * 페이지네이션 응답
     */
    @Data
    @Builder
    public static class PageResponse {
        private List<ListItem> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}