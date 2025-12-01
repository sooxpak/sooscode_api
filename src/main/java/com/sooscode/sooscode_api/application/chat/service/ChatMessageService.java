package com.sooscode.sooscode_api.application.chat.service;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequestDto;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;

import java.util.List;

public interface ChatMessageService {
    void saveAndBroadcast(ChatMessageRequestDto chatMessageRequestDto);

    List<ChatMessageResponseDto> getHistoryByClassId(Long classId);

}
