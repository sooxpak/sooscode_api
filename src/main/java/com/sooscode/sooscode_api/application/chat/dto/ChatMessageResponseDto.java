package com.sooscode.sooscode_api.application.chat.dto;


import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDto {

    private Long chatId;
    private String content;
    private LocalDateTime createdAt;
    // 필요하면 나중에 createdAt도 추가하면 됨
    // private LocalDateTime createdAt;

    public static ChatMessageResponseDto from(ChatMessage entity) {
        return ChatMessageResponseDto.builder()
                .chatId(entity.getChatId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                // .createdAt(entity.getCreatedAt())
                .build();
    }
}
