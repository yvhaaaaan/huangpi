package com.huangpi.platform.repository;

import com.huangpi.platform.domain.ReviewStatus;
import com.huangpi.platform.domain.ReviewTargetType;
import com.huangpi.platform.domain.ReviewTaskEntity;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewTaskRepository extends JpaRepository<ReviewTaskEntity, Long>, JpaSpecificationExecutor<ReviewTaskEntity> {
    Optional<ReviewTaskEntity> findByTargetTypeAndTargetIdAndStatus(ReviewTargetType targetType, Long targetId, ReviewStatus status);
    long countByStatus(ReviewStatus status);
    long countByStatusAndReviewedAtBetween(ReviewStatus status, Instant start, Instant end);
}
