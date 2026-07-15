package com.huangpi.platform.user.dto;

import com.huangpi.platform.product.dto.CategoryResponse;
import com.huangpi.platform.product.dto.ProductSummaryResponse;
import java.util.List;
import java.util.Map;

public record HomeResponse(
        List<Map<String, Object>> banners,
        List<CategoryResponse> featuredCategories,
        List<ProductSummaryResponse> recommendedProducts,
        List<MapPointResponse> recommendedMapPoints,
        List<ActivitySummaryResponse> activities) {
}
