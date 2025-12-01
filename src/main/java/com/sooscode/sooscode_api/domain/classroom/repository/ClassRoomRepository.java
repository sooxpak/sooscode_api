package com.sooscode.sooscode_api.domain.classroom.repository;

import com.sooscode.sooscode_api.domain.classroom.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
}