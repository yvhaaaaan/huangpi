package com.huangpi.platform.repository;

import com.huangpi.platform.domain.TravelRouteEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRouteRepository extends JpaRepository<TravelRouteEntity, Long> {
    List<TravelRouteEntity> findByEnabledTrueOrderBySortOrderAscIdAsc();
}
