package com.huangpi.platform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountLoginRequest(
        @NotBlank @Size(max = 80) String account,
        @NotBlank @Size(max = 120) String password) {
}
