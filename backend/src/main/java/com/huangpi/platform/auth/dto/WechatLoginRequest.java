package com.huangpi.platform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WechatLoginRequest(@NotBlank @Size(max = 256) String code) {
}
