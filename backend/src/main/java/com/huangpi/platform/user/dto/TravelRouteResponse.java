package com.huangpi.platform.user.dto;

import java.util.List;

public record TravelRouteResponse(
        String id,
        String title,
        String summary,
        String duration,
        String suitable,
        String imageUrl,
        List<String> mapPointIds) {
}
