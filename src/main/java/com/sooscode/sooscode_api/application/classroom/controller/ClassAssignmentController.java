package com.sooscode.sooscode_api.application.classroom.controller;

import com.sooscode.sooscode_api.application.classroom.dto.ClassAssignmentRequest;
import com.sooscode.sooscode_api.application.classroom.service.ClassAssignmentService;
import com.sooscode.sooscode_api.domain.classroom.entity.ClassAssignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
@Slf4j
public class ClassAssignmentController {
    private final ClassAssignmentService classAssignmentService;

    @PostMapping("/assignment")
    public ResponseEntity<?> addClassAssignment(@RequestBody ClassAssignmentRequest rq) {

        log.info("addClassAssignment Controller 실행");

        classAssignmentService.addClassAssignment(rq);

        return ResponseEntity.ok("add class assignment successfully");
    }
}
