package com.sooscode.sooscode_api.application.admin.service;

import com.sooscode.sooscode_api.application.admin.dto.AdminClassRequest;
import com.sooscode.sooscode_api.application.admin.dto.AdminClassResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminClassServiceImpl implements AdminClassService {
    @Override
    public AdminClassResponse.Detail createClass(AdminClassRequest.Create request) {
        return null;
    }

    @Override
    public AdminClassResponse.Detail updateClass(Long classId, AdminClassRequest.Update request) {
        return null;
    }

    @Override
    public void deleteClass(Long classId) {

    }

    @Override
    public AdminClassResponse.PageResponse getClassList(AdminClassRequest.SearchFilter filter, int page, int size) {
        return null;
    }

    @Override
    public AdminClassResponse.Detail getClassDetail(Long classId) {
        return null;
    }

    @Override
    public void assignInstructor(Long classId, AdminClassRequest.AssignInstructor request) {

    }

    @Override
    public void assignStudents(Long classId, AdminClassRequest.AssignStudents request) {

    }
}
