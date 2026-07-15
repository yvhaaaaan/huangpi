package com.huangpi.platform.merchant.dto;

import java.time.Instant;
import java.util.List;

public record MerchantProductResponse(
        String id,
        String title,
        String categoryId,
        String categoryName,
        String summary,
        String content,
        String coverFileId,
        String coverUrl,
        List<String> imageFileIds,
        String address,
        String contactPhone,
        String businessHours,
        String status,
        String statusLabel,
        String rejectReason,
        long views,
        Instant updatedAt) {
}
