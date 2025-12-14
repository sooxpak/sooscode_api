package com.sooscode.sooscode_api.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 클래스 WebSocket 관련 비즈니스 로직
 * - 수업 종료 시 전체 연결 해제
 * - 특정 사용자 강제 퇴장
 * - 접속자 수 조회 등
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClassSocketService {

    private final WebSocketSessionRegistry sessionRegistry;
    private final SimpMessagingTemplate messagingTemplate;


    /**
     * 클래스 전체 연결 해제 (수업 종료 시)
     * - 접속 중인 모든 클라이언트에게 종료 메시지 전송
     * - Redis 멤버 목록 정리
     */
    public void disconnectAll(Long classId) {

        String classIdStr = String.valueOf(classId);
        Set<Object> members = sessionRegistry.getClassMembers(classIdStr);

        if (members.isEmpty()) {
            log.debug("No members to disconnect: classId={}", classId);
            return;
        }

        // 클라이언트에게 수업 종료 알림
        ClassEndMessage message = new ClassEndMessage(
                "CLASS_ENDED",
                "수업이 종료되었습니다."
        );

        messagingTemplate.convertAndSend(
                "/topic/class/" + classId + "/system",
                message
        );

        // Redis 멤버 목록 정리
        sessionRegistry.clearClassMembers(classIdStr);

        log.info("CLASS DISCONNECTED ALL — classId={}, memberCount={}", classId, members.size());
    }


    /**
     * 특정 사용자 강제 퇴장
     * - 해당 사용자에게 퇴장 알림 전송
     * - 클래스 멤버 목록에서 제거
     */
    public void kickUser(Long classId, Long userId, String reason) {

        String classIdStr = String.valueOf(classId);

        // 해당 사용자에게 퇴장 알림
        KickMessage message = new KickMessage(
                "KICKED",
                reason != null ? reason : "강사에 의해 퇴장되었습니다."
        );

        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/system",
                message
        );

        // 멤버 목록에서 제거
        sessionRegistry.leaveClass(classIdStr, userId);

        log.info("USER KICKED — classId={}, userId={}, reason={}", classId, userId, reason);
    }


    /**
     * 클래스 현재 접속자 수 조회
     */
    public int getMemberCount(Long classId) {
        Set<Object> members = sessionRegistry.getClassMembers(String.valueOf(classId));
        return members.size();
    }


    /**
     * 클래스 현재 접속자 목록 조회
     */
    public Set<Object> getMembers(Long classId) {
        return sessionRegistry.getClassMembers(String.valueOf(classId));
    }


    /**
     * 특정 사용자가 클래스에 접속 중인지 확인
     */
    public boolean isMemberConnected(Long classId, Long userId) {
        Set<Object> members = sessionRegistry.getClassMembers(String.valueOf(classId));
        return members.contains(userId);
    }


    // ==================== 메시지 DTO ====================

    public record ClassEndMessage(
            String type,
            String message
    ) {}

    public record KickMessage(
            String type,
            String message
    ) {}
}