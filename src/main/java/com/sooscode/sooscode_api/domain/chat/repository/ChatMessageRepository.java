package com.sooscode.sooscode_api.domain.chat.repository;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;
import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    // 🔥 방 별로 조회
    List<ChatMessage> findAllByClassRoom_ClassIdOrderByCreatedAtAsc(Long classId);
}
