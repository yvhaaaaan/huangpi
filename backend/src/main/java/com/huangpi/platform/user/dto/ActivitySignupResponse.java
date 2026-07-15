package com.huangpi.platform.user.dto;

import java.time.Instant;

public record ActivitySignupResponse(
        String id,
        String activityId,
        String activityTitle,
        String name,
        String phone,
        int peopleCount,
        String remark,
        String status,
        Instant createdAt) {
}
