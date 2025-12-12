package com.sooscode.sooscode_api.application.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageRequest;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageResponse;
import com.sooscode.sooscode_api.application.chat.dto.ChatMessageType;
import com.sooscode.sooscode_api.application.chat.dto.EnterOrExitResponse;
import com.sooscode.sooscode_api.domain.chatmessage.entity.ChatMessage;
import com.sooscode.sooscode_api.domain.chatmessage.repository.ChatMessageRepository;
import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.repository.ClassRoomRepository;
import com.sooscode.sooscode_api.domain.user.entity.User;
import com.sooscode.sooscode_api.domain.user.repository.UserRepository;
import com.sooscode.sooscode_api.global.api.exception.CustomException;
import com.sooscode.sooscode_api.global.api.status.ChatStatus;
import com.sooscode.sooscode_api.global.api.status.ClassRoomStatus;
import com.sooscode.sooscode_api.global.api.status.UserStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Builder
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request, Long userId) {

        // 유저/클래스 유효성 (기존대로 DB로 체크)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserStatus.NOT_FOUND));

        classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new CustomException(ClassRoomStatus.CLASS_NOT_FOUND));

        // reply 정보(있으면) Redis에서 가져와서 채우기
        Long replyToChatId = request.getReplyToChatId();
        String replyToName = null;
        String replyToContent = null;

        if (replyToChatId != null) {
            Object raw = redisTemplate.opsForValue().get(chatKey(replyToChatId));
            if (raw == null) throw new CustomException(ChatStatus.NOT_FOUND);

            ChatMessageResponse replyDto = objectMapper.convertValue(raw, ChatMessageResponse.class);

            // 다른 클래스 메시지에 답장 금지
            if (!replyDto.getClassId().equals(request.getClassId())) {
                throw new CustomException(ChatStatus.ACCESS_DENIED);
            }

            replyToName = replyDto.getName();
            replyToContent = replyDto.getContent();
        }

        //  chatId 발급 (Redis가 auto_increment 역할)
        Long chatId = redisTemplate.opsForValue().increment("ws:chat:seq");
        if (chatId == null) throw new CustomException(ChatStatus.NOT_FOUND);

        LocalDateTime now = LocalDateTime.now();
        long score = System.currentTimeMillis();

        //  DTO 생성 (너 DTO는 불변이라 new로 만들어야 함)
        ChatMessageResponse dto = new ChatMessageResponse(
                chatId,
                request.getClassId(),
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                request.getContent(),
                ChatMessageType.CHAT,
                now,
                replyToChatId,
                replyToName,
                replyToContent,
                false,
                0
        );

        //  1) 메시지 저장
        redisTemplate.opsForValue().set(chatKey(chatId), dto);

        //  2) 클래스 히스토리 인덱스 저장(시간순)
        redisTemplate.opsForZSet().add(classChatZsetKey(request.getClassId()), chatId.toString(), score);

        return dto;
    }

    private String chatKey(Long chatId) {
        return "ws:chat:" + chatId;
    }

    private String classChatZsetKey(Long classId) {
        return "ws:class:" + classId + ":chat:ids";
    }
    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findAllByClassRoom(Long classId) {

        String zsetKey = "ws:class:" + classId + ":chat:ids";

        // 1) 시간순 chatId 목록
        Set<Object> chatIds = redisTemplate.opsForZSet().range(zsetKey, 0, -1);
        if (chatIds == null || chatIds.isEmpty()) return List.of();

        // 2) msg key 목록
        List<String> keys = chatIds.stream()
                .map(String::valueOf)
                .map(id -> "ws:chat:" + id)
                .toList();

        // 3) 한 번에 가져오기
        List<Object> raws = redisTemplate.opsForValue().multiGet(keys);
        if (raws == null) return List.of();

        // 4) (A방법 핵심) Map/Object -> DTO 복원
        return raws.stream()
                .filter(Objects::nonNull)
                .map(raw -> objectMapper.convertValue(raw, ChatMessageResponse.class))
                .toList();
    }

    @Transactional
    @Override
    public void deleteMessage(Long classId, Long chatId, Long userId) {

        String chatKey = "ws:chat:" + chatId;

        Object raw = redisTemplate.opsForValue().get(chatKey);
        if (raw == null) {
            throw new CustomException(ChatStatus.NOT_FOUND);
        }

        ChatMessageResponse message = objectMapper.convertValue(raw, ChatMessageResponse.class);

        //  클래스 검증
        if (!message.getClassId().equals(classId)) {
            throw new CustomException(ChatStatus.ACCESS_DENIED);
        }

        //  본인 메시지인지 검증
        if (message.getUserId() == null || !message.getUserId().equals(userId)) {
            throw new CustomException(ChatStatus.ACCESS_DENIED);
        }

        //  이미 삭제됨이면 종료
        if (message.isDeleted()) {
            return;
        }

        //  소프트 삭제(덮어쓰기)
        redisTemplate.opsForValue().set(chatKey, message.delete());
    }

    @Override
    public EnterOrExitResponse enterchatRoom(Long userId, Long classRoomId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserStatus.NOT_FOUND));

        return new EnterOrExitResponse(
                user.getEmail(),
                user.getName()
        );
    }
    @Override
    public EnterOrExitResponse exitchatRoom(Long userId, Long classRoomId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserStatus.NOT_FOUND));

        return new EnterOrExitResponse(
                user.getEmail(),
                user.getName()
        );
    }
}
