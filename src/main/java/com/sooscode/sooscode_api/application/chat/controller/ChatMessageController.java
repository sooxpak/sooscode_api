package com.sooscode.sooscode_api.application.chat.controller;/*package com.sooscode.sooscode_api.application.chat.controller;

import com.sooscode.sooscode_api.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;



@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatService chatService;


    @MessageMapping("/chat.send")
    public void send(ChatMessage message) {
        chatService.saveAndBroadcast(message);
    }
}*/
