package com.huangpi.platform.admin.dto;

import jakarta.validation.constraints.Size;

public record ReviewActionRequest(@Size(max = 500) String comment, @Size(max = 500) String reason) {
}
