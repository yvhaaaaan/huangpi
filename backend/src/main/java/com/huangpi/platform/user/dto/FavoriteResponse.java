package com.huangpi.platform.user.dto;

import java.time.Instant;

public record FavoriteResponse(
        String id,
        String targetType,
        String targetId,
        String title,
        String summary,
        String coverUrl,
        Instant createdAt) {
}
