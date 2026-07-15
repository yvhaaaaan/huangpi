package com.huangpi.platform.security;

import com.huangpi.platform.domain.UserRole;
import java.time.Instant;

public record SessionPrincipal(Long userId, UserRole role, Long merchantId, String jti, Instant expiresAt) {
}
