package com.sooscode.sooscode_api.domain.chat.service;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequestDto;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;
import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;
import com.sooscode.sooscode_api.domain.chat.repository.ChatMessageRepository;
import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.repository.ClassRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageDomainServiceImpl implements ChatMessageDomainService {

    private final ChatMessageRepository chatMessagerepository;
    private final ClassRoomRepository classroomrepository;

    @Override
    @Transactional
    public ChatMessage saveMessage(ChatMessageRequestDto requestDto) {

        // DTO: (classId, content) 있다고 가정
        // classId -> ClassRoom 프록시(레퍼런스)만 받아옴 (실제 SELECT 안 날릴 수도 있음)
        ClassRoom classRoomRef = classroomrepository.getReferenceById(requestDto.getClassId());

        ChatMessage message = ChatMessage.builder()
                .classRoom(classRoomRef)
                .content(requestDto.getContent())
                .build();

        return chatMessagerepository.save(message);
    }


    @Override
    public List<ChatMessageResponseDto> getHistoryByClassId(Long classId) {
        return chatMessagerepository
                .findAllByClassRoom_ClassIdOrderByCreatedAtAsc(classId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }
}
