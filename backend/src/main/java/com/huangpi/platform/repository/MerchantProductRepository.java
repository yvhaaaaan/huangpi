package com.huangpi.platform.repository;

import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.MerchantProductEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantProductRepository extends JpaRepository<MerchantProductEntity, Long>, JpaSpecificationExecutor<MerchantProductEntity> {
    Optional<MerchantProductEntity> findByIdAndMerchantId(Long id, Long merchantId);
    Page<MerchantProductEntity> findByMerchantId(Long merchantId, Pageable pageable);
    List<MerchantProductEntity> findByMerchantIdOrderByUpdatedAtDesc(Long merchantId);
    long countByMerchantIdAndStatus(Long merchantId, ContentStatus status);
    long countByStatus(ContentStatus status);
    long countByPublishedAtBetween(Instant start, Instant end);
}
