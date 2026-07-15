package com.huangpi.platform.product.dto;

public record ProductSummaryResponse(
        String id,
        String categoryId,
        String categoryCode,
        String categoryName,
        String title,
        String summary,
        String coverUrl,
        String merchantName,
        String address,
        String status) {
}
