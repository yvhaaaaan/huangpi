package com.huangpi.platform.repository;

import com.huangpi.platform.domain.MapPointEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapPointRepository extends JpaRepository<MapPointEntity, Long> {
    List<MapPointEntity> findByEnabledTrueOrderBySortOrderAscIdAsc();
}
