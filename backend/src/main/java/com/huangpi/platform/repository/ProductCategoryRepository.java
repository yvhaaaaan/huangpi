package com.huangpi.platform.repository;

import com.huangpi.platform.domain.CategoryStatus;
import com.huangpi.platform.domain.ProductCategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    Optional<ProductCategoryEntity> findByCode(String code);
    List<ProductCategoryEntity> findByStatusOrderByPriorityLevelAscSortOrderAsc(CategoryStatus status);
    List<ProductCategoryEntity> findAllByOrderByPriorityLevelAscSortOrderAsc();
}
