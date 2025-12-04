package com.sooscode.sooscode_api.application.classroom.service;

import com.sooscode.sooscode_api.application.classroom.dto.ClassAssignmentRequest;
import org.springframework.stereotype.Service;

@Service
public interface ClassAssignmentService {
    public void addClassAssignment(ClassAssignmentRequest rq);
}
