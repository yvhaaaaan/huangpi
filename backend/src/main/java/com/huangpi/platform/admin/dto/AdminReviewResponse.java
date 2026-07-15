package com.huangpi.platform.admin.dto;

import java.time.Instant;

public record AdminReviewResponse(
        String id,
        String targetType,
        String targetId,
        String merchantId,
        String title,
        String owner,
        String categoryName,
        String summary,
        String coverUrl,
        String status,
        Instant submittedAt,
        String reviewComment) {
}
