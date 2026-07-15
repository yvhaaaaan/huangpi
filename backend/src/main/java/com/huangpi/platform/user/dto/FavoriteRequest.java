package com.huangpi.platform.user.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteRequest(@NotBlank String targetType, @NotBlank String targetId) {
}
