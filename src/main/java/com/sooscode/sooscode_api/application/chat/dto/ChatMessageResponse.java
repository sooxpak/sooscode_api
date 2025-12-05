package com.sooscode.sooscode_api.application.chat.dto;

import com.sooscode.sooscode_api.domain.chatmessage.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {
    private Long classId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getClassRoom().getClassId(),
                message.getUser() != null ? message.getUser().getUserId() : null,
                message.getContent(),
                message.getCreatedAt()
        );
    }

}
