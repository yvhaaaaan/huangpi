package com.huangpi.platform.admin.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminCategoryRequest(
        @Pattern(regexp = "[a-z][a-z0-9_]{1,59}") String code,
        @Size(max = 80) String name,
        Integer priorityLevel,
        Integer sort,
        @Pattern(regexp = "enabled|disabled") String status) {
}
