package com.sooscode.sooscode_api.application.compile.service;

import com.sooscode.sooscode_api.application.compile.dto.CompileResultResponse;
import com.sooscode.sooscode_api.application.compile.dto.CompileRunResponse;
import com.sooscode.sooscode_api.global.exception.CustomException;
import com.sooscode.sooscode_api.global.exception.errorcode.CompileErrorCode;
import com.sooscode.sooscode_api.infra.worker.CodeBlacklistFilter;
import com.sooscode.sooscode_api.infra.worker.CompileFutureStore;
import com.sooscode.sooscode_api.infra.worker.CompileWorkerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CompileServiceImpl implements CompileService {

    private final CompileWorkerClient compileWorkerClient;
    private final CompileFutureStore compileFutureStore;

    @Value("${app.urls.backend}")
    private String backendUrl;

    @Override
    public CompletableFuture<CompileResultResponse> runCode(String code) {
        /**
         * [1] 코드 유효성 검사
         * - 금지된 패턴/보안 위험 요소 등을 검증한다.
         * - 유효하지 않으면 예외 발생.
         */
        CodeBlacklistFilter.validate(code);
        String jobId = UUID.randomUUID().toString();
        /**
         * [2] 컴파일 워커 서버에 실행 요청 및 콜백 url 생성
         * - 비동기 워커 서버에 POST 요청을 보내 jobId 를 획득한다.
         * - jobId는 이후 결과 조회에 사용된다.
         */
        try {
            String callbackUrl =
                    backendUrl + "/api/compile/callback/" + jobId;
            CompletableFuture<CompileResultResponse> future =
                    compileFutureStore.createFuture(jobId);

            compileWorkerClient.requestCompile(jobId, code, callbackUrl);

            return future;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            compileFutureStore.completeFuture(jobId,
                    new CompileResultResponse("TIMEOUT", "워커 서버 통신 오류 발생"));
            throw new CustomException(CompileErrorCode.WORKER_TIMEOUT);
        }
    }
}

