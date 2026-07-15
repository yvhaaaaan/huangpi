package com.huangpi.platform.merchant;

import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.merchant.dto.MerchantDashboardResponse;
import com.huangpi.platform.merchant.dto.MerchantProductResponse;
import com.huangpi.platform.merchant.dto.MerchantProductUpsertRequest;
import com.huangpi.platform.merchant.dto.MerchantProfileResponse;
import com.huangpi.platform.merchant.dto.MerchantProfileUpdateRequest;
import com.huangpi.platform.security.SessionPrincipal;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<MerchantDashboardResponse> dashboard(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(merchantService.dashboard(principal));
    }

    @GetMapping("/profile")
    public ApiResponse<MerchantProfileResponse> profile(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(merchantService.profile(principal));
    }

    @PutMapping("/profile")
    public ApiResponse<MerchantProfileResponse> updateProfile(
            @AuthenticationPrincipal SessionPrincipal principal,
            @Valid @RequestBody MerchantProfileUpdateRequest request) {
        return ApiResponse.success(merchantService.updateProfile(principal, request));
    }

    @PostMapping("/profile/submit")
    public ApiResponse<Void> submitProfile(@AuthenticationPrincipal SessionPrincipal principal) {
        merchantService.submitProfile(principal);
        return ApiResponse.success();
    }

    @GetMapping("/products")
    public ApiResponse<PageResponse<MerchantProductResponse>> products(
            @AuthenticationPrincipal SessionPrincipal principal,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(merchantService.products(principal, page, pageSize));
    }

    @PostMapping("/products")
    public ApiResponse<MerchantProductResponse> createProduct(
            @AuthenticationPrincipal SessionPrincipal principal,
            @Valid @RequestBody MerchantProductUpsertRequest request) {
        return ApiResponse.success(merchantService.createProduct(principal, request));
    }

    @GetMapping("/products/{id}")
    public ApiResponse<MerchantProductResponse> product(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.success(merchantService.product(principal, id));
    }

    @PutMapping("/products/{id}")
    public ApiResponse<MerchantProductResponse> updateProduct(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody MerchantProductUpsertRequest request) {
        return ApiResponse.success(merchantService.updateProduct(principal, id, request));
    }

    @PostMapping("/products/{id}/submit")
    public ApiResponse<Void> submitProduct(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id) {
        merchantService.submitProduct(principal, id);
        return ApiResponse.success();
    }

    @PostMapping("/products/{id}/close")
    public ApiResponse<Void> closeProduct(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id) {
        merchantService.closeProduct(principal, id);
        return ApiResponse.success();
    }

    @GetMapping("/messages")
    public ApiResponse<List<MerchantDashboardResponse.Message>> messages(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(merchantService.dashboard(principal).messages());
    }

    @GetMapping("/todos")
    public ApiResponse<List<MerchantDashboardResponse.Todo>> todos(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(merchantService.dashboard(principal).todos());
    }
}
