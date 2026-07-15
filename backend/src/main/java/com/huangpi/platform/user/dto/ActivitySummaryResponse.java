package com.huangpi.platform.user.dto;

import java.time.Instant;

public record ActivitySummaryResponse(
        String id,
        String status,
        String type,
        String title,
        String summary,
        String coverUrl,
        String place,
        Instant startAt,
        Instant endAt,
        int signupLimit,
        long signupCount) {
}
