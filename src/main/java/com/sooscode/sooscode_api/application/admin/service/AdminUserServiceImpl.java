package com.sooscode.sooscode_api.application.admin.service;

import com.sooscode.sooscode_api.application.admin.dto.AdminUserRequest;
import com.sooscode.sooscode_api.application.admin.dto.AdminUserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Override
    public AdminUserResponse.InstructorCreated createInstructor(AdminUserRequest.CreateInstructor request) {
        return null;
    }

    @Override
    public AdminUserResponse.PageResponse getUserList(AdminUserRequest.SearchFilter filter, int page, int size) {
        return null;
    }

    @Override
    public AdminUserResponse.Detail getUserDetail(Long userId) {
        return null;
    }

    @Override
    public List<AdminUserResponse.LoginHistory> getLoginHistory(Long userId, int limit) {
        return List.of();
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public void toggleUserStatus(Long userId, boolean isActive) {

    }

    @Override
    public void changeUserRole(Long userId, AdminUserRequest.ChangeRole request) {

    }

    @Override
    public AdminUserResponse.BulkCreateResult bulkCreateUsers(AdminUserRequest.BulkCreate request) {
        return null;
    }

    @Override
    public byte[] exportUsersToExcel(AdminUserRequest.SearchFilter filter) {
        return new byte[0];
    }

    @Override
    public AdminUserResponse.Statistics getUserStatistics() {
        return null;
    }
}
