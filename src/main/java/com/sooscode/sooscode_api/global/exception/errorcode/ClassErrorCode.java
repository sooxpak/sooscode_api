package com.sooscode.sooscode_api.global.exception.errorcode;

import com.sooscode.sooscode_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClassErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "CLASS_001", "클래스를 찾을 수 없습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "CLASS_002", "클래스 접근 권한이 없습니다"),
    NOT_STARTED(HttpStatus.FORBIDDEN, "CLASS_003", "아직 시작되지 않은 클래스입니다"),
    ALREADY_ENDED(HttpStatus.FORBIDDEN, "CLASS_004", "이미 종료된 클래스입니다"),
    OFFLINE(HttpStatus.FORBIDDEN, "CLASS_005", "오프라인 클래스인 경우 온라인 접속 불가합니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}