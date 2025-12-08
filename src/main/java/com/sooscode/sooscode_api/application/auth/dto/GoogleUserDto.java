package com.sooscode.sooscode_api.application.auth.dto;

public record GoogleUserDto(
        String email,
        String name,
        String picture
) {}