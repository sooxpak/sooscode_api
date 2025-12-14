package com.sooscode.sooscode_api.domain.classroom.scheduler;

import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import com.sooscode.sooscode_api.domain.classroom.repository.ClassRoomRepository;
import com.sooscode.sooscode_api.global.websocket.ClassSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 수업 자동 종료 스케줄러
 * - 1분마다 종료 시간이 지난 수업 체크
 * - WebSocket 연결 해제 및 종료일 기록
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ClassRoomCleanupScheduler {

    private final ClassRoomRepository classRoomRepository;
    private final ClassSocketService classSocketService;


    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cleanupClasses() {

        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        List<ClassRoom> targets =
                classRoomRepository.findClassesToCleanup(today, nowTime);

        if (targets.isEmpty()) {
            return;
        }

        for (ClassRoom classRoom : targets) {
            try {
                // 접속 중인 모든 사용자 연결 해제
                classSocketService.disconnectAll(classRoom.getClassId());

                // 종료일 기록
                classRoom.setLastClosedDate(today);

                log.info(
                        "[CLASS CLEANUP] classId={} closedDate={}",
                        classRoom.getClassId(),
                        today
                );

            } catch (Exception e) {
                log.error(
                        "[CLASS CLEANUP FAILED] classId={}",
                        classRoom.getClassId(),
                        e
                );
            }
        }
    }
}