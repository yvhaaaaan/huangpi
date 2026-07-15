package com.huangpi.platform.product;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.domain.CategoryStatus;
import com.huangpi.platform.domain.ProductCategoryEntity;
import com.huangpi.platform.product.dto.CategoryResponse;
import com.huangpi.platform.repository.ProductCategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final ProductCategoryRepository categoryRepository;

    public CategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> enabledCategories() {
        return categoryRepository.findByStatusOrderByPriorityLevelAscSortOrderAsc(CategoryStatus.ENABLED)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> allCategories() {
        return categoryRepository.findAllByOrderByPriorityLevelAscSortOrderAsc()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductCategoryEntity requireEnabled(Long id) {
        ProductCategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品品类不存在"));
        if (category.getStatus() != CategoryStatus.ENABLED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "产品品类已停用");
        }
        return category;
    }

    @Transactional(readOnly = true)
    public ProductCategoryEntity requireAny(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品品类不存在"));
    }

    public CategoryResponse toResponse(ProductCategoryEntity category) {
        return new CategoryResponse(category.getId().toString(), category.getCode(), category.getName(), category.getPriorityLevel(), category.getSortOrder());
    }
}
