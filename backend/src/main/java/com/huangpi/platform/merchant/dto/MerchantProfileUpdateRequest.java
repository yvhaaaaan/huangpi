package com.huangpi.platform.merchant.dto;

import jakarta.validation.constraints.Size;

public record MerchantProfileUpdateRequest(
        @Size(max = 120) String name,
        @Size(max = 60) String owner,
        @Size(max = 30) String phone,
        @Size(max = 240) String address,
        @Size(max = 80) String businessHours,
        @Size(max = 2000) String intro,
        String coverFileId) {
}
