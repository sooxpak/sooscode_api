package com.sooscode.sooscode_api.application.classroom.dto;

import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.enums.ClassMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoomDetailResponse {

    private Long classId;
    private String title;
    private boolean isOnline;
    private ClassMode mode;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // 강의를 듣는 총 인원 수
    private Integer totalParticipantCount;

    // 접속자 권한 정보
    private boolean isInstructor;

    public static ClassRoomDetailResponse from(ClassRoom classRoom, int totalParticipantCount, boolean isInstructor) {
        return ClassRoomDetailResponse.builder()
                .classId(classRoom.getClassId())
                .title(classRoom.getTitle())
                .isOnline(classRoom.isOnline())
                .mode(classRoom.getMode())
                .totalParticipantCount(totalParticipantCount)
                .isInstructor(isInstructor)
                .build();
    }
}