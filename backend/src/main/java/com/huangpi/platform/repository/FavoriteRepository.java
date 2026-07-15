package com.huangpi.platform.repository;

import com.huangpi.platform.domain.FavoriteEntity;
import com.huangpi.platform.domain.FavoriteTargetType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, FavoriteTargetType targetType, Long targetId);
    List<FavoriteEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<FavoriteEntity> findByIdAndUserId(Long id, Long userId);
}
