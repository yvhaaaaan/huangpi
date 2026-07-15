package com.huangpi.platform.auth.dto;

public record SessionResponse(String token, long expiresAt, UserInfo user) {

    public record UserInfo(String id, String nickname, String role, String merchantId) {
    }
}
