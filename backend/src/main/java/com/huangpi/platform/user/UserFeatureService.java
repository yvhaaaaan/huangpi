package com.huangpi.platform.user;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.domain.ActivityEntity;
import com.huangpi.platform.domain.ActivitySignupEntity;
import com.huangpi.platform.domain.ActivitySignupStatus;
import com.huangpi.platform.domain.ActivityStatus;
import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.FavoriteEntity;
import com.huangpi.platform.domain.FavoriteTargetType;
import com.huangpi.platform.domain.MapPointEntity;
import com.huangpi.platform.file.FileService;
import com.huangpi.platform.repository.ActivityRepository;
import com.huangpi.platform.repository.ActivitySignupRepository;
import com.huangpi.platform.repository.FavoriteRepository;
import com.huangpi.platform.repository.MapPointRepository;
import com.huangpi.platform.repository.MerchantProductRepository;
import com.huangpi.platform.repository.TravelRouteRepository;
import com.huangpi.platform.repository.UserRepository;
import com.huangpi.platform.security.SessionPrincipal;
import com.huangpi.platform.user.dto.ActivityDetailResponse;
import com.huangpi.platform.user.dto.ActivitySignupRequest;
import com.huangpi.platform.user.dto.ActivitySignupResponse;
import com.huangpi.platform.user.dto.ActivitySummaryResponse;
import com.huangpi.platform.user.dto.FavoriteRequest;
import com.huangpi.platform.user.dto.FavoriteResponse;
import com.huangpi.platform.user.dto.MapPointResponse;
import com.huangpi.platform.user.dto.TravelRouteResponse;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFeatureService {

    private final MapPointRepository mapPointRepository;
    private final TravelRouteRepository travelRouteRepository;
    private final ActivityRepository activityRepository;
    private final ActivitySignupRepository activitySignupRepository;
    private final FavoriteRepository favoriteRepository;
    private final MerchantProductRepository productRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public UserFeatureService(
            MapPointRepository mapPointRepository,
            TravelRouteRepository travelRouteRepository,
            ActivityRepository activityRepository,
            ActivitySignupRepository activitySignupRepository,
            FavoriteRepository favoriteRepository,
            MerchantProductRepository productRepository,
            UserRepository userRepository,
            FileService fileService) {
        this.mapPointRepository = mapPointRepository;
        this.travelRouteRepository = travelRouteRepository;
        this.activityRepository = activityRepository;
        this.activitySignupRepository = activitySignupRepository;
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public List<MapPointResponse> mapPoints() {
        return mapPointRepository.findByEnabledTrueOrderBySortOrderAscIdAsc().stream()
                .map(this::toMapPoint)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TravelRouteResponse> routes() {
        return travelRouteRepository.findByEnabledTrueOrderBySortOrderAscIdAsc().stream()
                .map(route -> new TravelRouteResponse(
                        route.getId().toString(),
                        route.getTitle(),
                        route.getSummary(),
                        route.getDuration(),
                        route.getSuitable(),
                        blankIfNull(route.getImageUrl()),
                        route.getMapPointIds().stream().map(String::valueOf).toList()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<ActivitySummaryResponse> activities(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        var pageData = activityRepository.findByStatus(
                ActivityStatus.PUBLISHED,
                PageRequest.of(safePage - 1, safePageSize, Sort.by(Sort.Direction.ASC, "startAt")));
        return PageResponse.from(pageData, pageData.getContent().stream().map(this::toActivitySummary).toList());
    }

    @Transactional(readOnly = true)
    public ActivityDetailResponse activity(Long id) {
        ActivityEntity activity = publishedActivity(id);
        long signupCount = registeredPeople(activity.getId());
        return new ActivityDetailResponse(
                activity.getId().toString(),
                activityDisplayStatus(activity, signupCount),
                activity.getType(),
                activity.getTitle(),
                activity.getSummary(),
                activity.getContent(),
                blankIfNull(activity.getCoverUrl()),
                activity.getPlace(),
                activity.getStartAt(),
                activity.getEndAt(),
                activity.getSignupLimit(),
                signupCount,
                signupAvailable(activity, signupCount));
    }

    @Transactional
    public ActivitySignupResponse signup(SessionPrincipal principal, Long activityId, ActivitySignupRequest request) {
        ActivityEntity activity = publishedActivity(activityId);
        if (activity.getEndAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.CONFLICT, "活动已结束，无法报名");
        }
        if (activitySignupRepository.existsByActivityIdAndUserId(activityId, principal.userId())) {
            throw new BusinessException(ErrorCode.CONFLICT, "你已报名该活动");
        }
        long registeredPeople = registeredPeople(activityId);
        if (activity.getSignupLimit() > 0 && registeredPeople + request.peopleCount() > activity.getSignupLimit()) {
            throw new BusinessException(ErrorCode.CONFLICT, "活动剩余名额不足");
        }
        var user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        ActivitySignupEntity signup = new ActivitySignupEntity();
        signup.setActivity(activity);
        signup.setUser(user);
        signup.setName(request.name().trim());
        signup.setPhone(request.phone());
        signup.setPeopleCount(request.peopleCount());
        signup.setRemark(request.remark());
        signup.setStatus(ActivitySignupStatus.REGISTERED);
        return toSignup(activitySignupRepository.save(signup));
    }

    @Transactional(readOnly = true)
    public List<ActivitySignupResponse> mySignups(SessionPrincipal principal) {
        return activitySignupRepository.findByUserIdOrderByCreatedAtDesc(principal.userId()).stream()
                .map(this::toSignup)
                .toList();
    }

    @Transactional
    public FavoriteResponse addFavorite(SessionPrincipal principal, FavoriteRequest request) {
        FavoriteTargetType targetType = parseTargetType(request.targetType());
        Long targetId = parseId(request.targetId());
        if (favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(principal.userId(), targetType, targetId)) {
            throw new BusinessException(ErrorCode.CONFLICT, "该内容已收藏");
        }
        FavoriteResponse target = resolveFavoriteTarget(null, targetType, targetId, null);
        var user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setUser(user);
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        FavoriteEntity saved = favoriteRepository.save(favorite);
        return new FavoriteResponse(
                saved.getId().toString(), target.targetType(), target.targetId(), target.title(),
                target.summary(), target.coverUrl(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> myFavorites(SessionPrincipal principal) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(principal.userId()).stream()
                .map(favorite -> resolveFavoriteTarget(
                        favorite.getId(), favorite.getTargetType(), favorite.getTargetId(), favorite.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void removeFavorite(SessionPrincipal principal, Long id) {
        FavoriteEntity favorite = favoriteRepository.findByIdAndUserId(id, principal.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "收藏记录不存在"));
        favoriteRepository.delete(favorite);
    }

    private FavoriteResponse resolveFavoriteTarget(
            Long favoriteId, FavoriteTargetType targetType, Long targetId, Instant createdAt) {
        String responseId = favoriteId == null ? "" : favoriteId.toString();
        return switch (targetType) {
            case PRODUCT -> productRepository.findById(targetId)
                    .filter(product -> product.getStatus() == ContentStatus.PUBLISHED)
                    .map(product -> new FavoriteResponse(
                            responseId,
                            "product",
                            targetId.toString(),
                            product.getTitle(),
                            product.getSummary(),
                            fileService.resolveUrl(product.getCoverFileId()),
                            createdAt))
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
            case ACTIVITY -> activityRepository.findByIdAndStatus(targetId, ActivityStatus.PUBLISHED)
                    .map(activity -> new FavoriteResponse(
                            responseId,
                            "activity",
                            targetId.toString(),
                            activity.getTitle(),
                            activity.getSummary(),
                            blankIfNull(activity.getCoverUrl()),
                            createdAt))
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "活动不存在"));
            case MAP_POINT -> mapPointRepository.findById(targetId)
                    .filter(MapPointEntity::isEnabled)
                    .map(point -> new FavoriteResponse(
                            responseId,
                            "map_point",
                            targetId.toString(),
                            point.getTitle(),
                            point.getSummary(),
                            blankIfNull(point.getImageUrl()),
                            createdAt))
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "地图点位不存在"));
        };
    }

    private ActivitySummaryResponse toActivitySummary(ActivityEntity activity) {
        long signupCount = registeredPeople(activity.getId());
        return new ActivitySummaryResponse(
                activity.getId().toString(),
                activityDisplayStatus(activity, signupCount),
                activity.getType(),
                activity.getTitle(),
                activity.getSummary(),
                blankIfNull(activity.getCoverUrl()),
                activity.getPlace(),
                activity.getStartAt(),
                activity.getEndAt(),
                activity.getSignupLimit(),
                signupCount);
    }

    private ActivitySignupResponse toSignup(ActivitySignupEntity signup) {
        return new ActivitySignupResponse(
                signup.getId().toString(),
                signup.getActivity().getId().toString(),
                signup.getActivity().getTitle(),
                signup.getName(),
                signup.getPhone(),
                signup.getPeopleCount(),
                signup.getRemark(),
                signup.getStatus().name().toLowerCase(Locale.ROOT),
                signup.getCreatedAt());
    }

    private MapPointResponse toMapPoint(MapPointEntity point) {
        return new MapPointResponse(
                point.getId().toString(),
                point.getTitle(),
                point.getType(),
                point.getSummary(),
                point.getAddress(),
                point.getLatitude(),
                point.getLongitude(),
                point.getDuration(),
                blankIfNull(point.getImageUrl()));
    }

    private ActivityEntity publishedActivity(Long id) {
        return activityRepository.findByIdAndStatus(id, ActivityStatus.PUBLISHED)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "活动不存在"));
    }

    private long registeredPeople(Long activityId) {
        return activitySignupRepository.sumPeopleCount(activityId, ActivitySignupStatus.REGISTERED);
    }

    private boolean signupAvailable(ActivityEntity activity, long signupCount) {
        return activity.getEndAt().isAfter(Instant.now())
                && (activity.getSignupLimit() <= 0 || signupCount < activity.getSignupLimit());
    }

    private String activityDisplayStatus(ActivityEntity activity, long signupCount) {
        if (activity.getEndAt().isBefore(Instant.now())) return "ended";
        if (activity.getSignupLimit() > 0 && signupCount >= activity.getSignupLimit()) return "full";
        return "signup_open";
    }

    private FavoriteTargetType parseTargetType(String value) {
        String normalized = value.trim().toLowerCase(Locale.ROOT).replace('-', '_');
        return switch (normalized) {
            case "product" -> FavoriteTargetType.PRODUCT;
            case "activity" -> FavoriteTargetType.ACTIVITY;
            case "map_point", "mappoint", "map" -> FavoriteTargetType.MAP_POINT;
            default -> throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的收藏类型");
        };
    }

    private Long parseId(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "目标 ID 格式错误");
        }
    }

    private String blankIfNull(String value) {
        return value == null ? "" : value;
    }
}
