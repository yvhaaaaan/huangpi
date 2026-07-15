package com.huangpi.platform.user.dto;

import java.time.Instant;

public record ActivityDetailResponse(
        String id,
        String status,
        String type,
        String title,
        String summary,
        String content,
        String coverUrl,
        String place,
        Instant startAt,
        Instant endAt,
        int signupLimit,
        long signupCount,
        boolean signupAvailable) {
}
