package com.huangpi.platform.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductDetailResponse(
        String id,
        String categoryId,
        String categoryCode,
        String categoryName,
        String title,
        String summary,
        String content,
        String coverUrl,
        List<String> imageUrls,
        String merchantName,
        String address,
        String contactPhone,
        String businessHours,
        BigDecimal latitude,
        BigDecimal longitude,
        String status,
        Instant publishedAt) {
}
