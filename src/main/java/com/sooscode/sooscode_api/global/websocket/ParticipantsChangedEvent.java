package com.sooscode.sooscode_api.global.websocket;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 참가자 변경 이벤트
 * - 클래스 입장/퇴장 시 발행
 */
@Getter
public class ParticipantsChangedEvent extends ApplicationEvent {

    private final Long classId;

    public ParticipantsChangedEvent(Object source, Long classId) {
        super(source);
        this.classId = classId;
    }
}