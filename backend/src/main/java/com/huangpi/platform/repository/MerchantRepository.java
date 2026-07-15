package com.huangpi.platform.repository;

import com.huangpi.platform.domain.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {
    Optional<MerchantEntity> findByUserId(Long userId);
}
