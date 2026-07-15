package com.huangpi.platform.config;

import com.huangpi.platform.domain.AccountStatus;
import com.huangpi.platform.domain.AccountUserEntity;
import com.huangpi.platform.domain.ActivityEntity;
import com.huangpi.platform.domain.ActivityStatus;
import com.huangpi.platform.domain.CategoryStatus;
import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.MapPointEntity;
import com.huangpi.platform.domain.MerchantEntity;
import com.huangpi.platform.domain.MerchantProductEntity;
import com.huangpi.platform.domain.ProductCategoryEntity;
import com.huangpi.platform.domain.ReviewStatus;
import com.huangpi.platform.domain.ReviewTargetType;
import com.huangpi.platform.domain.ReviewTaskEntity;
import com.huangpi.platform.domain.TravelRouteEntity;
import com.huangpi.platform.domain.UserEntity;
import com.huangpi.platform.domain.UserRole;
import com.huangpi.platform.repository.AccountUserRepository;
import com.huangpi.platform.repository.ActivityRepository;
import com.huangpi.platform.repository.MapPointRepository;
import com.huangpi.platform.repository.MerchantProductRepository;
import com.huangpi.platform.repository.MerchantRepository;
import com.huangpi.platform.repository.ProductCategoryRepository;
import com.huangpi.platform.repository.ReviewTaskRepository;
import com.huangpi.platform.repository.TravelRouteRepository;
import com.huangpi.platform.repository.UserRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountUserRepository accountUserRepository;
    private final MerchantRepository merchantRepository;
    private final ProductCategoryRepository categoryRepository;
    private final MerchantProductRepository productRepository;
    private final ReviewTaskRepository reviewTaskRepository;
    private final MapPointRepository mapPointRepository;
    private final TravelRouteRepository travelRouteRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            AccountUserRepository accountUserRepository,
            MerchantRepository merchantRepository,
            ProductCategoryRepository categoryRepository,
            MerchantProductRepository productRepository,
            ReviewTaskRepository reviewTaskRepository,
            MapPointRepository mapPointRepository,
            TravelRouteRepository travelRouteRepository,
            ActivityRepository activityRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountUserRepository = accountUserRepository;
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.reviewTaskRepository = reviewTaskRepository;
        this.mapPointRepository = mapPointRepository;
        this.travelRouteRepository = travelRouteRepository;
        this.activityRepository = activityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {

        ProductCategoryEntity oilTea = category("oil_tea", "油茶", 1, 10);
        ProductCategoryEntity simiaoRice = category("simiao_rice", "丝苗米", 1, 20);
        ProductCategoryEntity hakkaFood = category("hakka_food", "客家食品", 2, 30);
        category("agricultural_product", "农副产品", 2, 40);
        category("cultural_creative", "文创伴手礼", 2, 50);

        UserEntity adminUser = user("黄陂镇文旅管理员", UserRole.ADMIN);
        account("admin", "123456", adminUser);

        UserEntity merchantUser = user("黄陂特色产品商家", UserRole.MERCHANT);
        account("merchant", "123456", merchantUser);

        MerchantEntity merchant = new MerchantEntity();
        merchant.setUser(merchantUser);
        merchant.setName("黄陂镇特色农产品合作社");
        merchant.setOwner("陈店长");
        merchant.setPhone("13800138000");
        merchant.setAddress("广东省兴宁市黄陂镇振兴路 18 号");
        merchant.setBusinessHours("08:30-18:00");
        merchant.setIntro("展示黄陂油茶、丝苗米和客家食品，提供产品介绍、产地参观和研学接待服务。");
        merchant.setStatus(ContentStatus.PUBLISHED);
        merchantRepository.save(merchant);

        publishedProduct(merchant, oilTea, "黄陂客家传统油茶", "本地山茶籽与客家传统工艺制作，茶香醇厚。");
        publishedProduct(merchant, simiaoRice, "黄陂丝苗米", "本地种植加工，米粒细长、饭香自然。");

        MerchantProductEntity pendingProduct = product(merchant, hakkaFood, "客家手工米果", "使用本地糯米制作，保留传统手工风味。");
        pendingProduct.setStatus(ContentStatus.PENDING);
        productRepository.save(pendingProduct);

        ReviewTaskEntity task = new ReviewTaskEntity();
        task.setTargetType(ReviewTargetType.MERCHANT_PRODUCT);
        task.setTargetId(pendingProduct.getId());
        task.setMerchantId(merchant.getId());
        task.setSubmitterUserId(merchantUser.getId());
        task.setStatus(ReviewStatus.PENDING);
        task.setSubmittedAt(Instant.now());
        reviewTaskRepository.save(task);
        }

        seedUserExperienceContent();
    }

    private void seedUserExperienceContent() {
        if (mapPointRepository.count() == 0) {
            mapPointRepository.save(mapPoint(
                    "黄陂油茶体验点", "油茶特色", "观看油茶制作流程，参与冲煮和品鉴。",
                    "广东省兴宁市黄陂镇游客服务中心旁", "24.3638200", "115.8741600", "60 分钟", 10));
            mapPointRepository.save(mapPoint(
                    "黄陂丝苗米示范基地", "丝苗米", "了解丝苗米种植、加工和产地故事。",
                    "广东省兴宁市黄陂镇农产品展示中心", "24.3712600", "115.8835200", "50 分钟", 20));
            mapPointRepository.save(mapPoint(
                    "客家古村落", "客家文旅", "串联传统建筑、村史展陈和乡土文化。",
                    "广东省兴宁市黄陂镇古村片区", "24.3569300", "115.8667400", "70 分钟", 30));
            mapPointRepository.save(mapPoint(
                    "非遗体验工坊", "非遗体验", "观看传统手作技艺，也可预约体验课堂。",
                    "广东省兴宁市黄陂镇非遗工坊", "24.3598800", "115.8709100", "50 分钟", 40));
        }

        List<MapPointEntity> points = mapPointRepository.findByEnabledTrueOrderBySortOrderAscIdAsc();
        if (travelRouteRepository.count() == 0 && !points.isEmpty()) {
            TravelRouteEntity route = new TravelRouteEntity();
            route.setTitle("黄陂特色产业与客家文化一日线");
            route.setSummary("串联油茶、丝苗米、客家古村和非遗工坊，集中了解黄陂特色产业与文化。");
            route.setDuration("一日");
            route.setSuitable("亲子、研学与团队");
            route.setImageUrl("");
            route.setMapPointIds(points.stream().map(MapPointEntity::getId).toList());
            route.setEnabled(true);
            route.setSortOrder(10);
            travelRouteRepository.save(route);
        }

        if (activityRepository.count() == 0) {
            Instant startAt = Instant.now().plus(Duration.ofDays(14));
            ActivityEntity activity = new ActivityEntity();
            activity.setTitle("黄陂油茶与丝苗米产地体验日");
            activity.setType("产业体验");
            activity.setSummary("走进油茶工坊和丝苗米基地，了解两类重点特色产品的制作与种植过程。");
            activity.setContent("上午参观油茶工坊并参与品鉴，下午前往丝苗米示范基地了解种植和加工流程。");
            activity.setCoverUrl("");
            activity.setPlace("黄陂镇游客服务中心");
            activity.setStartAt(startAt);
            activity.setEndAt(startAt.plus(Duration.ofHours(7)));
            activity.setSignupLimit(30);
            activity.setStatus(ActivityStatus.PUBLISHED);
            activityRepository.save(activity);
        }
    }

    private MapPointEntity mapPoint(
            String title,
            String type,
            String summary,
            String address,
            String latitude,
            String longitude,
            String duration,
            int sortOrder) {
        MapPointEntity point = new MapPointEntity();
        point.setTitle(title);
        point.setType(type);
        point.setSummary(summary);
        point.setAddress(address);
        point.setLatitude(new BigDecimal(latitude));
        point.setLongitude(new BigDecimal(longitude));
        point.setDuration(duration);
        point.setImageUrl("");
        point.setEnabled(true);
        point.setSortOrder(sortOrder);
        return point;
    }

    private ProductCategoryEntity category(String code, String name, int priorityLevel, int sort) {
        ProductCategoryEntity category = new ProductCategoryEntity();
        category.setCode(code);
        category.setName(name);
        category.setPriorityLevel(priorityLevel);
        category.setSortOrder(sort);
        category.setStatus(CategoryStatus.ENABLED);
        return categoryRepository.save(category);
    }

    private UserEntity user(String nickname, UserRole role) {
        UserEntity user = new UserEntity();
        user.setNickname(nickname);
        user.setRole(role);
        user.setStatus(AccountStatus.ENABLED);
        return userRepository.save(user);
    }

    private void account(String username, String password, UserEntity user) {
        AccountUserEntity account = new AccountUserEntity();
        account.setUsername(username);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setUser(user);
        accountUserRepository.save(account);
    }

    private MerchantProductEntity product(MerchantEntity merchant, ProductCategoryEntity category, String title, String summary) {
        MerchantProductEntity product = new MerchantProductEntity();
        product.setMerchant(merchant);
        product.setCategory(category);
        product.setTitle(title);
        product.setSummary(summary);
        product.setContent(summary);
        product.setAddress(merchant.getAddress());
        product.setContactPhone(merchant.getPhone());
        product.setBusinessHours(merchant.getBusinessHours());
        product.setImageFileIds(List.of());
        return product;
    }

    private void publishedProduct(MerchantEntity merchant, ProductCategoryEntity category, String title, String summary) {
        MerchantProductEntity product = product(merchant, category, title, summary);
        product.setStatus(ContentStatus.PUBLISHED);
        product.setPublishedAt(Instant.now());
        productRepository.save(product);
    }
}
