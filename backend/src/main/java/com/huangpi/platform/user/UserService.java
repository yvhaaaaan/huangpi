package com.huangpi.platform.user;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.product.CategoryService;
import com.huangpi.platform.product.ProductService;
import com.huangpi.platform.repository.UserRepository;
import com.huangpi.platform.security.SessionPrincipal;
import com.huangpi.platform.user.dto.HomeResponse;
import com.huangpi.platform.user.dto.MeResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserFeatureService userFeatureService;

    public UserService(
            UserRepository userRepository,
            CategoryService categoryService,
            ProductService productService,
            UserFeatureService userFeatureService) {
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userFeatureService = userFeatureService;
    }

    @Transactional(readOnly = true)
    public MeResponse me(SessionPrincipal principal) {
        var user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        return new MeResponse(user.getId().toString(), user.getNickname(), user.getAvatarUrl(), user.getRole().name().toLowerCase());
    }

    @Transactional(readOnly = true)
    public HomeResponse home() {
        var categories = categoryService.enabledCategories();
        var products = productService.listPublished(null, null, 1, 6).list();
        var mapPoints = userFeatureService.mapPoints().stream().limit(4).toList();
        var activities = userFeatureService.activities(1, 4).list();
        return new HomeResponse(List.of(), categories, products, mapPoints, activities);
    }
}
