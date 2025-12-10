package com.sooscode.sooscode_api.application.auth.dto;

import com.sooscode.sooscode_api.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String email;
    private String name;
    private String role;
}
