package com.sooscode.sooscode_api.global.exception.errorcode;

import com.sooscode.sooscode_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    OK(HttpStatus.OK, "COMMON_000", "요청을 성공적으로 처리했습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_002", "인증이 필요합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_003", "접근 권한이 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_004", "요청한 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_005", "서버 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}