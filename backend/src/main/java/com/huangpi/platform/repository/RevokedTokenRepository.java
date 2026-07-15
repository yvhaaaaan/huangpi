package com.huangpi.platform.repository;

import com.huangpi.platform.domain.RevokedTokenEntity;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedTokenEntity, Long> {
    boolean existsByJti(String jti);
    void deleteByExpiresAtBefore(Instant instant);
}
