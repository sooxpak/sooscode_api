package com.sooscode.sooscode_api.application.compile.service;

import com.sooscode.sooscode_api.application.compile.dto.CompileResultResponse;
import com.sooscode.sooscode_api.application.compile.dto.CompileRunResponse;
import com.sooscode.sooscode_api.global.exception.CustomException;
import com.sooscode.sooscode_api.global.exception.errorcode.CompileErrorCode;
import com.sooscode.sooscode_api.infra.worker.CodeBlacklistFilter;
import com.sooscode.sooscode_api.infra.worker.CompileWorkerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompileServiceImpl implements CompileService {

    private final CompileWorkerClient compileWorkerClient;

    @Override
    public CompileResultResponse runCode(String code) {
        /**
         * [1] 코드 유효성 검사
         * - 금지된 패턴/보안 위험 요소 등을 검증한다.
         * - 유효하지 않으면 예외 발생.
         */
        CodeBlacklistFilter.validate(code);
        /**
         * [2] 컴파일 워커 서버에 실행 요청
         * - 비동기 워커 서버에 POST 요청을 보내 jobId 를 획득한다.
         * - jobId는 이후 결과 조회에 사용된다.
         */
        CompileRunResponse runResponse = compileWorkerClient.requestCompile(code);
        String jobId = runResponse.getJobId();

        if (jobId == null || jobId.isBlank()) {
            throw new CustomException(CompileErrorCode.WORKER_ERROR);
        }

        /**
         * [3] 워커 서버 결과 polling
         * - 워커 서버의 비동기 작업이 완료될 때까지 상태를 반복 조회한다.
         * - 최대 30회 / 500ms 간격 → 약 15초까지 대기.
         * - 상태가 PENDING이 아니면 즉시 결과 반환.
         */
        for (int i = 0; i < 30; i++) {
            CompileResultResponse result = compileWorkerClient.getCompileResult(jobId);

            if (!"PENDING".equals(result.getStatus())) {

                if ("COMPILE_ERROR".equals(result.getStatus())){
                    throw new CustomException(CompileErrorCode.COMPILE_FAILED);
                }
                if ("RUN_ERROR".equals(result.getStatus())){
                    throw new CustomException(CompileErrorCode.RUNTIME_ERROR);
                }
                return result;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        /**
         * [4] Timeout 처리
         * - 제한 시간(15초) 동안 작업이 완료되지 않으면 TIMEOUT 상태를 반환한다.
         */
        return new CompileResultResponse("TIMEOUT", "Execution timed out");
    }
}
