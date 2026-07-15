package com.huangpi.platform.user;

import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.product.ProductService;
import com.huangpi.platform.product.dto.ProductDetailResponse;
import com.huangpi.platform.product.dto.ProductSummaryResponse;
import com.huangpi.platform.security.SessionPrincipal;
import com.huangpi.platform.user.dto.ActivityDetailResponse;
import com.huangpi.platform.user.dto.ActivitySignupRequest;
import com.huangpi.platform.user.dto.ActivitySignupResponse;
import com.huangpi.platform.user.dto.ActivitySummaryResponse;
import com.huangpi.platform.user.dto.FavoriteRequest;
import com.huangpi.platform.user.dto.FavoriteResponse;
import com.huangpi.platform.user.dto.HomeResponse;
import com.huangpi.platform.user.dto.MapPointResponse;
import com.huangpi.platform.user.dto.MeResponse;
import com.huangpi.platform.user.dto.TravelRouteResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final UserFeatureService userFeatureService;

    public UserController(UserService userService, ProductService productService, UserFeatureService userFeatureService) {
        this.userService = userService;
        this.productService = productService;
        this.userFeatureService = userFeatureService;
    }

    @GetMapping("/me")
    public ApiResponse<MeResponse> me(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(userService.me(principal));
    }

    @GetMapping("/home")
    public ApiResponse<HomeResponse> home() {
        return ApiResponse.success(userService.home());
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<ProductSummaryResponse>> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(productService.listPublished(null, keyword, page, pageSize));
    }

    @GetMapping("/contents")
    public ApiResponse<PageResponse<ProductSummaryResponse>> contents(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(productService.listPublished(category, null, page, pageSize));
    }

    @GetMapping("/contents/{id}")
    public ApiResponse<ProductDetailResponse> content(@PathVariable Long id) {
        return ApiResponse.success(productService.getPublished(id));
    }

    @GetMapping("/map/points")
    public ApiResponse<List<MapPointResponse>> mapPoints() {
        return ApiResponse.success(userFeatureService.mapPoints());
    }

    @GetMapping("/routes")
    public ApiResponse<List<TravelRouteResponse>> routes() {
        return ApiResponse.success(userFeatureService.routes());
    }

    @GetMapping("/activities")
    public ApiResponse<PageResponse<ActivitySummaryResponse>> activities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(userFeatureService.activities(page, pageSize));
    }

    @GetMapping("/activities/{id}")
    public ApiResponse<ActivityDetailResponse> activity(@PathVariable Long id) {
        return ApiResponse.success(userFeatureService.activity(id));
    }

    @PostMapping("/activities/{id}/signups")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<ActivitySignupResponse> signup(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ActivitySignupRequest request) {
        return ApiResponse.success(userFeatureService.signup(principal, id, request));
    }

    @GetMapping("/me/signups")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<ActivitySignupResponse>> mySignups(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(userFeatureService.mySignups(principal));
    }

    @GetMapping("/me/favorites")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<FavoriteResponse>> myFavorites(@AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(userFeatureService.myFavorites(principal));
    }

    @PostMapping("/favorites")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<FavoriteResponse> addFavorite(
            @AuthenticationPrincipal SessionPrincipal principal,
            @Valid @RequestBody FavoriteRequest request) {
        return ApiResponse.success(userFeatureService.addFavorite(principal, request));
    }

    @DeleteMapping("/favorites/{id}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> removeFavorite(
            @AuthenticationPrincipal SessionPrincipal principal,
            @PathVariable Long id) {
        userFeatureService.removeFavorite(principal, id);
        return ApiResponse.success(null);
    }
}
