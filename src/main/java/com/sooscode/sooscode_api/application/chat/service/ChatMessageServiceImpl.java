package com.sooscode.sooscode_api.application.chat.service;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequestDto;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;
import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;
import com.sooscode.sooscode_api.domain.chat.service.ChatMessageDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageDomainService chatMessageDomainService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void saveAndBroadcast(ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessage saved = chatMessageDomainService.saveMessage(chatMessageRequestDto);
        
        //브로드캐스트
        String destination = "/topic/chat/" + saved.getClassRoom().getClassId();
        simpMessagingTemplate.convertAndSend(destination, saved);
    }

    @Override
    public List<ChatMessageResponseDto> getHistoryByClassId(Long classId) {
        return chatMessageDomainService.getHistoryByClassId(classId);
    }
}
