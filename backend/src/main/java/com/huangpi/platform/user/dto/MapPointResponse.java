package com.huangpi.platform.user.dto;

import java.math.BigDecimal;

public record MapPointResponse(
        String id,
        String title,
        String type,
        String summary,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String duration,
        String imageUrl) {
}
