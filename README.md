Sooscode API Server
Sooscode 플랫폼의 핵심 비즈니스 로직을 담당하는 메인 백엔드 서버입니다.

사용자 인증, 강의실 관리, 실시간 채팅 및 코드 동기화, 그리고 컴파일 워커와의 오케스트레이션을 담당합니다.

Spring Boot 3.x 기반의 MSA(Microservices Architecture) 지향 구조로 설계되었으며,

안정적인 데이터 처리와 WebSocket 기반의 실시간 협업 환경을 제공합니다.

전체 아키텍처 (Architecture)
API 서버는 클라이언트와 다양한 인프라/마이크로서비스 사이의 중계 및 처리 역할을 수행합니다.

코드 스니펫

graph TD
    Client[Client (React)] -->|HTTP/REST| API[API Server]
    Client -->|WebSocket/STOMP| API
    
    subgraph Infrastructure
        API -->|Data Persist| MySQL[(MySQL)]
        API -->|Auth & Cache| Redis[(Redis)]
        API -->|File Storage| S3[AWS S3]
    end
    
    subgraph External Services
        API -->|Compile Request| Worker[Compile Worker]
        Worker -->|Result Callback| API
        API -->|Token Issuance| LiveKit[LiveKit Server]
        API -->|OAuth2| Google[Google Auth]
    end

    기술 스택 (Tech Stack)CategoryTechnologyDescriptionFrameworkSpring Boot 3.2Java 17 기반의 백엔드 프레임워크DatabaseMySQL 8.0사용자, 강의실, 채팅 이력 등 영구 데이터 저장Cache & SessionRedisJWT Refresh Token 관리 (Blacklist), 세션 캐싱Real-timeWebSocket (STOMP)실시간 채팅, 동시 편집(코드 동기화), 알림SecuritySpring SecurityJWT + OAuth2 (Google) 기반 인증/인가Media ServerLiveKit실시간 화상/음성 강의 기능을 위한 토큰 발급 및 제어StorageAWS S3프로필 이미지, 강의 자료 파일 업로드✨ 핵심 기능 (Key Features)1. 보안 인증 시스템 (Security & Auth)JWT (Json Web Token): Access Token(Stateless)과 Refresh Token(Redis 저장)을 이용한 이중 토큰 구조.OAuth 2.0: Google 소셜 로그인 지원.Redis Blacklist: 로그아웃 시 남은 유효기간 동안 Access Token을 블랙리스트 처리하여 보안 강화.2. 실시간 협업 (Real-time Collaboration)WebSocket + STOMP: /ws 엔드포인트를 통해 연결, /sub, /pub 구조로 메시지 브로커 활용.Code Synchronization: 강의실 내 참여자 간 코드 에디터 상태 실시간 동기화.Presence: 사용자 입장/퇴장 및 타이핑 상태 실시간 감지.3. 컴파일 오케스트레이션 (Compile Orchestration)Delegation: 클라이언트의 코드 실행 요청을 받아 유효성을 검증하고 Worker 서버로 위임.Async Callback Handling: Worker로부터 비동기적으로 실행 결과를 수신(CompletableFuture)하여 클라이언트에게 응답.4. 화상 강의 (LiveKit Integration)Token Generation: LiveKit 서버 접속을 위한 보안 토큰 동적 생성 및 발급.Webhook Handling: LiveKit 서버의 이벤트(방 생성, 종료 등)를 수신하여 강의실 상태 동기화.🔄 주요 처리 흐름 (Process Flow)1. 로그인 프로세스 (Login)클라이언트 로그인 요청 (Email/PW or Google)AuthService에서 검증 및 JWT 발급 (Access + Refresh)Refresh Token은 Redis에 저장 (TTL 설정)Access Token은 쿠키/헤더로 반환2. 코드 실행 프로세스 (Code Execution)Request: 사용자가 코드 실행 요청 (POST /api/compile/run)Validation: 코드 내 악성 키워드(System.exit 등) 및 길이 검증.Delegation: CompileWorkerClient를 통해 Worker 서버로 HTTP 요청 전송.Waiting: CompileFutureStore에 jobId를 키로 하는 Future 객체 생성 후 대기 (Timeout 설정).Callback: Worker가 실행 완료 후 /api/compile/callback/{jobId} 호출.Response: 대기 중이던 Future가 완료되며 클라이언트에게 최종 결과 반환.📂 디렉토리 구조 (Directory Structure)도메인형 구조를 채택하여 관련 로직을 응집도 있게 관리합니다.src/main/java/com/sooscode/sooscode_api

