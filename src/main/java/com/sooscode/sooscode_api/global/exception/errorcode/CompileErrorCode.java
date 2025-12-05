package com.sooscode.sooscode_api.global.exception.errorcode;

import com.sooscode.sooscode_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompileErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, " COMPILE_001", "코드를 입력해주세요");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
