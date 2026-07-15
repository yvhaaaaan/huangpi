package com.huangpi.platform.merchant.dto;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record MerchantProductUpsertRequest(
        @Size(max = 120) String title,
        String categoryId,
        @Size(max = 500) String summary,
        @Size(max = 10000) String content,
        String coverFileId,
        List<String> imageFileIds,
        @Size(max = 240) String address,
        @Size(max = 30) String contactPhone,
        @Size(max = 80) String businessHours,
        BigDecimal latitude,
        BigDecimal longitude) {
}
