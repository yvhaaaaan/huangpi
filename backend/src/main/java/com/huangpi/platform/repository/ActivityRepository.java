package com.huangpi.platform.repository;

import com.huangpi.platform.domain.ActivityEntity;
import com.huangpi.platform.domain.ActivityStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    Page<ActivityEntity> findByStatus(ActivityStatus status, Pageable pageable);
    Optional<ActivityEntity> findByIdAndStatus(Long id, ActivityStatus status);
}
