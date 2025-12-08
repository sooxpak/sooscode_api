package com.sooscode.sooscode_api.application.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String name;
    private String role;
    private String profileImage;
}
