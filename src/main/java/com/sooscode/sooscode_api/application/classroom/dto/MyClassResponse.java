package com.sooscode.sooscode_api.application.classroom.dto;

import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyClassResponse {

    private Long classId;
    private String title;
    private String thumbnailUrl;
    private String teacherName;

    public MyClassResponse(Long classId, String title, String thumbnailUrl, String teacherName) {
        this.classId = classId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.teacherName = teacherName;
    }
}

