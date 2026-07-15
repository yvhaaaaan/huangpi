package com.huangpi.platform.admin;

import com.huangpi.platform.admin.dto.AdminCategoryRequest;
import com.huangpi.platform.admin.dto.AdminCategoryResponse;
import com.huangpi.platform.admin.dto.AdminDashboardResponse;
import com.huangpi.platform.admin.dto.AdminReviewResponse;
import com.huangpi.platform.admin.dto.MerchantSummaryResponse;
import com.huangpi.platform.admin.dto.ReviewActionRequest;
import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.security.SessionPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<AdminDashboardResponse> dashboard() {
        return ApiResponse.success(adminService.dashboard());
    }

    @GetMapping("/reviews")
    public ApiResponse<PageResponse<AdminReviewResponse>> reviews(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String targetType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(adminService.reviews(status, targetType, page, pageSize));
    }

    @GetMapping("/reviews/{id}")
    public ApiResponse<AdminReviewResponse> review(@PathVariable Long id) {
        return ApiResponse.success(adminService.review(id));
    }

    @PostMapping("/reviews/{id}/approve")
    public ApiResponse<Void> approve(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody(required = false) ReviewActionRequest request) {
        adminService.approve(principal, id, request == null ? "" : request.comment());
        return ApiResponse.success();
    }

    @PostMapping("/reviews/{id}/reject")
    public ApiResponse<Void> reject(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ReviewActionRequest request) {
        adminService.reject(principal, id, request.reason());
        return ApiResponse.success();
    }

    @PostMapping("/reviews/{id}/close")
    public ApiResponse<Void> close(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody(required = false) ReviewActionRequest request) {
        adminService.close(principal, id, request == null ? "" : request.reason());
        return ApiResponse.success();
    }

    @GetMapping("/merchants")
    public ApiResponse<PageResponse<MerchantSummaryResponse>> merchants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(adminService.merchants(page, pageSize));
    }

    @GetMapping("/product-categories")
    public ApiResponse<List<AdminCategoryResponse>> categories() {
        return ApiResponse.success(adminService.categories());
    }

    @PostMapping("/product-categories")
    public ApiResponse<AdminCategoryResponse> createCategory(@Valid @RequestBody AdminCategoryRequest request) {
        return ApiResponse.success(adminService.createCategory(request));
    }

    @PutMapping("/product-categories/{id}")
    public ApiResponse<AdminCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody AdminCategoryRequest request) {
        return ApiResponse.success(adminService.updateCategory(id, request));
    }

    @GetMapping("/audit-logs")
    public ApiResponse<PageResponse<Map<String, Object>>> auditLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(adminService.auditLogs(page, pageSize));
    }
}
