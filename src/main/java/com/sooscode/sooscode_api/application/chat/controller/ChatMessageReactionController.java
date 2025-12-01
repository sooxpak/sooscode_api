package com.sooscode.sooscode_api.application.chat.controller;/*package com.sooscode.sooscode_api.application.chat.controller;

import com.sooscode.sooscode_api.domain.chat.service.ChatMessageReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/reactions")
public class ChatMessageReactionController {

    private final ChatMessageReactionService reactionService;

    // POST /api/chat/reactions/like?messageId=1&reactor=닉네임
    @PostMapping("/like")
    public void like(@RequestParam Long messageId,
                     @RequestParam String reactor) {
        reactionService.like(messageId, reactor);
    }

    // GET /api/chat/reactions/summary?messageId=1
    @GetMapping("/summary")
    public Map<String, Object> summary(@RequestParam Long messageId) {
        long likeCount = reactionService.countLikes(messageId);
        List<String> reactors = reactionService.getReactors(messageId);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("reactors", reactors);
        return result;
    }
}
*/