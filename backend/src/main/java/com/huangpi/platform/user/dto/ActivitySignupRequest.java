package com.huangpi.platform.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActivitySignupRequest(
        @NotBlank @Size(max = 60) String name,
        @NotBlank @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误") String phone,
        @Min(1) @Max(20) int peopleCount,
        @Size(max = 500) String remark) {
}
