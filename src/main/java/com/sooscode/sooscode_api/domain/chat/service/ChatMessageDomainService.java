package com.sooscode.sooscode_api.domain.chat.service;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequestDto;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;
import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageDomainService {

    ChatMessage saveMessage(ChatMessageRequestDto requestDto);

    List<ChatMessageResponseDto> getHistoryByClassId(Long classId);
}
