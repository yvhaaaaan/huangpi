package com.huangpi.platform.product;

import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.product.dto.CategoryResponse;
import com.huangpi.platform.product.dto.ProductDetailResponse;
import com.huangpi.platform.product.dto.ProductSummaryResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/product-categories")
    public ApiResponse<List<CategoryResponse>> categories() {
        return ApiResponse.success(categoryService.enabledCategories());
    }

    @GetMapping("/products")
    public ApiResponse<PageResponse<ProductSummaryResponse>> products(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(productService.listPublished(categoryCode, keyword, page, pageSize));
    }

    @GetMapping("/products/{id}")
    public ApiResponse<ProductDetailResponse> product(@PathVariable Long id) {
        return ApiResponse.success(productService.getPublished(id));
    }
}
