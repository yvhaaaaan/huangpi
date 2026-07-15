package com.huangpi.platform.merchant.dto;

public record MerchantProfileResponse(
        String id,
        String name,
        String owner,
        String phone,
        String address,
        String businessHours,
        String intro,
        String coverFileId,
        String coverUrl,
        String status,
        String rejectReason) {
}
