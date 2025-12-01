package com.sooscode.sooscode_api.application.chat.controller;

import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequestDto;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponseDto;
import com.sooscode.sooscode_api.application.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:5173", "http://10.41.0.89:5173" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatMessageService chatMessageService;

    //  classID 파라미터 받아서 그 방 히스토리만 리턴
    @GetMapping("/history")
    public List<ChatMessageResponseDto> getHistory(@RequestParam("classId") Long classId) {
        return chatMessageService.getHistoryByClassId(classId);
    }
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequestDto chatMessageRequestDto) {
        chatMessageService.saveAndBroadcast(chatMessageRequestDto);
    }
}
