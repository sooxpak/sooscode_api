package com.sooscode.sooscode_api.application.compile.controller;

import com.sooscode.sooscode_api.application.compile.dto.CompileResultResponse;
import com.sooscode.sooscode_api.application.compile.dto.CompileRunRequest;
import com.sooscode.sooscode_api.application.compile.service.CompileService;
import com.sooscode.sooscode_api.infra.worker.CompileFutureStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/compile")
@RequiredArgsConstructor
public class CompileController {

    private final CompileService compileService;
    private final CompileFutureStore compileFutureStore;

    /**
     *  post 코드 실행 run 요청  result 반환
     **/
    @PostMapping("/run")
    public CompletableFuture<ResponseEntity<CompileResultResponse>> run(@Valid @RequestBody CompileRunRequest request) {

        CompletableFuture<CompileResultResponse> resultFuture = compileService.runCode(request.getCode());
        return resultFuture.thenApply(ResponseEntity::ok);
    }
    /**
     * [신규] 워커 서버로부터 컴파일 결과를 수신하는 콜백 엔드포인트 (Webhook)
     */
    @PostMapping("/callback/{jobId}")
    public ResponseEntity<Void> receiveCallback(
            @PathVariable String jobId,
            @RequestBody CompileResultResponse result) {

        // 워커로부터 결과를 받으면 Future Store의 대기 중인 퓨처를 완료
        compileFutureStore.completeFuture(jobId, result);

        // 워커 서버 에서 받음
        return ResponseEntity.ok().build();
    }
}