package com.sooscode.sooscode_api.application.auth.service;

import com.sooscode.sooscode_api.domain.user.entity.User;

public interface UserService {

    /**
     * 신규 유저 저장
     */
    User saveUser(User user);

    /**
     * 이메일로 유저 조회
     */
    User findByEmail(String email);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
}
