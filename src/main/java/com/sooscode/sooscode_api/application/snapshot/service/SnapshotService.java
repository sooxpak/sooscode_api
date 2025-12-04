package com.sooscode.sooscode_api.application.snapshot.service;

import com.sooscode.sooscode_api.application.snapshot.dto.SnapshotSaveRequest;
import com.sooscode.sooscode_api.domain.snapshot.entity.CodeSnapshot;

public interface SnapshotService {

    CodeSnapshot saveCodeSnapshot(SnapshotSaveRequest snapshotSaveRequest, Long userId);


}
