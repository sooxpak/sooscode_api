package com.sooscode.sooscode_api.global.exception.errorcode;

import com.sooscode.sooscode_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_001", "파일을 찾을 수 없습니다"),
    METADATA_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_002", "파일 메타데이터 저장 실패"),
    METADATA_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_003", "파일 메타데이터 수정 실패"),
    METADATA_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_004", "파일 메타데이터 삭제 실패"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "FILE_005", "잘못된 파일 요청입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}