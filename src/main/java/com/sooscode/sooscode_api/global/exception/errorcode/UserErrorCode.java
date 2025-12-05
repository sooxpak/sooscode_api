package com.sooscode.sooscode_api.global.exception.errorcode;

import com.sooscode.sooscode_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다"),
    SUSPENDED(HttpStatus.FORBIDDEN, "USER_002", "정지된 사용자입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}