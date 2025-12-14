package com.sooscode.sooscode_api.global.websocket;

import com.sooscode.sooscode_api.global.security.CustomUserDetails;
import com.sooscode.sooscode_api.global.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * STOMP 메시지 인터셉터
 * - CONNECT: 세션 생성, 중복 접속 처리
 * - SUBSCRIBE: 채팅 채널 구독 시 클래스 입장 처리
 * - DISCONNECT: 세션 정리, 클래스 퇴장 처리
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StompSessionInterceptor implements ChannelInterceptor {

    private final WebSocketSessionRegistry sessionRegistry;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            handleConnect(accessor);
        }
        else if (StompCommand.SUBSCRIBE.equals(command)) {
            handleSubscribe(accessor);
        }
        else if (StompCommand.DISCONNECT.equals(command)) {
            handleDisconnect(accessor);
        }

        return message;
    }


    /**
     * CONNECT 처리
     * - JWT 인증 정보에서 userId 추출
     * - 중복 접속 시 기존 세션 정리
     * - 새 세션 등록
     */
    private void handleConnect(StompHeaderAccessor accessor) {

        if (!(accessor.getUser() instanceof UsernamePasswordAuthenticationToken auth)) {
            return;
        }
        if (!(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return;
        }

        Long userId = userDetails.getUser().getUserId();
        String sessionId = accessor.getSessionId();

        // 중복 접속 체크 및 기존 세션 정리
        String oldSessionId = sessionRegistry.getExistingSessionId(userId);

        if (oldSessionId != null && !oldSessionId.equals(sessionId)) {
            cleanupOldSession(oldSessionId, userId);
        }

        // 새 세션 등록
        sessionRegistry.registerSession(sessionId, userId);
        log.info("WS CONNECT — sessionId={}, userId={}", sessionId, userId);
    }


    /**
     * 기존 세션 정리 (중복 접속 시)
     */
    private void cleanupOldSession(String oldSessionId, Long userId) {

        // 기존 세션의 클래스 입장 정보 확인
        String oldClassId = sessionRegistry.getClassId(oldSessionId);

        if (oldClassId != null) {
            sessionRegistry.leaveClass(oldClassId, userId);
            log.info("FORCE CLASS LEAVE — classId={}, userId={}", oldClassId, userId);
        }

        // 기존 세션 삭제
        sessionRegistry.removeSession(oldSessionId);
        log.info("FORCE DISCONNECT — oldSessionId={}, userId={}", oldSessionId, userId);
    }


    /**
     * SUBSCRIBE 처리
     * - 채팅 채널 구독 시에만 클래스 입장으로 처리
     * - 패턴: /topic/class/{classId}/chat
     */
    private void handleSubscribe(StompHeaderAccessor accessor) {

        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();

        if (!isChatChannel(destination)) {
            return;
        }

        Long userId = sessionRegistry.getUserId(sessionId);
        String classId = extractClassId(destination);

        if (userId == null || classId == null) {
            return;
        }

        sessionRegistry.joinClass(sessionId, classId, userId);
        log.info("CLASS JOIN — classId={}, userId={}, sessionId={}", classId, userId, sessionId);
    }


    /**
     * DISCONNECT 처리
     * - 클래스 퇴장 처리
     * - 세션 삭제
     */
    private void handleDisconnect(StompHeaderAccessor accessor) {

        String sessionId = accessor.getSessionId();

        Long userId = sessionRegistry.getUserId(sessionId);
        String classId = sessionRegistry.getClassId(sessionId);

        // 클래스 퇴장
        if (classId != null && userId != null) {
            sessionRegistry.leaveClass(classId, userId);
            log.info("CLASS LEAVE — classId={}, userId={}", classId, userId);
        }

        // 세션 삭제
        sessionRegistry.removeSession(sessionId);
        log.info("WS DISCONNECT — sessionId={}, userId={}", sessionId, userId);
    }


    /**
     * 채팅 채널인지 확인
     */
    private boolean isChatChannel(String destination) {
        return destination != null
                && destination.startsWith("/topic/class/")
                && destination.endsWith("/chat");
    }

    /**
     * destination에서 classId 추출
     * /topic/class/{classId}/chat → classId
     */
    private String extractClassId(String destination) {
        String[] parts = destination.split("/");
        return parts.length >= 4 ? parts[3] : null;
    }
}