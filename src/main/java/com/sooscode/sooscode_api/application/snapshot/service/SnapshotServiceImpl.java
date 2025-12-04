package com.sooscode.sooscode_api.application.snapshot.service;

import com.sooscode.sooscode_api.application.snapshot.dto.SnapshotSaveRequest;
import com.sooscode.sooscode_api.domain.chatmessage.repository.ChatMessageReactionRepository;
import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.repository.ClassRoomRepository;
import com.sooscode.sooscode_api.domain.snapshot.entity.CodeSnapshot;
import com.sooscode.sooscode_api.domain.snapshot.repository.CodeSnapshotRepository;
import com.sooscode.sooscode_api.domain.user.entity.User;
import com.sooscode.sooscode_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    private final CodeSnapshotRepository codeSnapshotRepository;
    private final UserRepository userRepository;
    private final ClassRoomRepository classRoomRepository;

    @Override
    public CodeSnapshot saveCodeSnapshot(SnapshotSaveRequest snapshotSaveRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("발견된 유저 없음"));

        ClassRoom classRoom = classRoomRepository.findById(snapshotSaveRequest.getClassId())
                .orElseThrow(()-> new RuntimeException("발견된 클래스 없음"));


        CodeSnapshot codeSnapshot = CodeSnapshot.builder()
                .user(user)
                .classRoom(classRoom)
                .title(snapshotSaveRequest.getTitle())
                .content(snapshotSaveRequest.getContent())
                .createdAt(snapshotSaveRequest.getCreatedAt())
                .build();

        return codeSnapshotRepository.save(codeSnapshot);
    }
}
