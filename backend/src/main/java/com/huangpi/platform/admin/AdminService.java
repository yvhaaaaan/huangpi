package com.huangpi.platform.admin;

import com.huangpi.platform.admin.dto.AdminCategoryRequest;
import com.huangpi.platform.admin.dto.AdminCategoryResponse;
import com.huangpi.platform.admin.dto.AdminDashboardResponse;
import com.huangpi.platform.admin.dto.AdminReviewResponse;
import com.huangpi.platform.admin.dto.MerchantSummaryResponse;
import com.huangpi.platform.audit.AuditLogService;
import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.domain.CategoryStatus;
import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.MerchantEntity;
import com.huangpi.platform.domain.MerchantProductEntity;
import com.huangpi.platform.domain.ProductCategoryEntity;
import com.huangpi.platform.domain.ReviewStatus;
import com.huangpi.platform.domain.ReviewTargetType;
import com.huangpi.platform.domain.ReviewTaskEntity;
import com.huangpi.platform.file.FileService;
import com.huangpi.platform.repository.AuditLogRepository;
import com.huangpi.platform.repository.MerchantProductRepository;
import com.huangpi.platform.repository.MerchantRepository;
import com.huangpi.platform.repository.ProductCategoryRepository;
import com.huangpi.platform.repository.ReviewTaskRepository;
import com.huangpi.platform.security.SessionPrincipal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ReviewTaskRepository reviewTaskRepository;
    private final MerchantProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final ProductCategoryRepository categoryRepository;
    private final AuditLogRepository auditLogRepository;
    private final AuditLogService auditLogService;
    private final FileService fileService;

    public AdminService(
            ReviewTaskRepository reviewTaskRepository,
            MerchantProductRepository productRepository,
            MerchantRepository merchantRepository,
            ProductCategoryRepository categoryRepository,
            AuditLogRepository auditLogRepository,
            AuditLogService auditLogService,
            FileService fileService) {
        this.reviewTaskRepository = reviewTaskRepository;
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.auditLogRepository = auditLogRepository;
        this.auditLogService = auditLogService;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse dashboard() {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        Instant start = LocalDate.now(zone).atStartOfDay(zone).toInstant();
        Instant end = start.plusSeconds(24 * 60 * 60);
        return new AdminDashboardResponse(
                reviewTaskRepository.countByStatus(ReviewStatus.PENDING),
                reviewTaskRepository.countByStatusAndReviewedAtBetween(ReviewStatus.APPROVED, start, end),
                merchantRepository.count(),
                productRepository.countByStatus(ContentStatus.PUBLISHED));
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminReviewResponse> reviews(String status, String targetType, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        Specification<ReviewTaskEntity> specification = Specification.where(null);
        if (status != null && !status.isBlank() && !"all".equalsIgnoreCase(status)) {
            ReviewStatus parsedStatus = parseReviewStatus(status);
            specification = specification.and((root, query, builder) -> builder.equal(root.get("status"), parsedStatus));
        }
        if (targetType != null && !targetType.isBlank()) {
            ReviewTargetType parsedType = parseTargetType(targetType);
            specification = specification.and((root, query, builder) -> builder.equal(root.get("targetType"), parsedType));
        }
        Page<ReviewTaskEntity> pageData = reviewTaskRepository.findAll(
                specification,
                PageRequest.of(safePage - 1, safePageSize, Sort.by(Sort.Direction.DESC, "submittedAt")));
        return PageResponse.from(pageData, pageData.getContent().stream().map(this::toReview).toList());
    }

    @Transactional(readOnly = true)
    public AdminReviewResponse review(Long id) {
        return toReview(requireReview(id));
    }

    @Transactional
    public void approve(SessionPrincipal principal, Long id, String comment) {
        ReviewTaskEntity task = requirePendingReview(id);
        if (task.getTargetType() == ReviewTargetType.MERCHANT_PRODUCT) {
            MerchantProductEntity product = requireProduct(task.getTargetId());
            product.setStatus(ContentStatus.PUBLISHED);
            product.setRejectReason(null);
            product.setPublishedAt(Instant.now());
        } else {
            MerchantEntity merchant = requireMerchant(task.getTargetId());
            merchant.setStatus(ContentStatus.PUBLISHED);
            merchant.setRejectReason(null);
        }
        finishReview(task, principal, ReviewStatus.APPROVED, comment);
        auditLogService.record(principal.userId(), "review_approve", task.getTargetType().name(), task.getTargetId(), Map.of("reviewId", task.getId()));
    }

    @Transactional
    public void reject(SessionPrincipal principal, Long id, String reason) {
        if (reason == null || reason.trim().length() < 5) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "驳回原因至少填写 5 个字");
        }
        ReviewTaskEntity task = requirePendingReview(id);
        if (task.getTargetType() == ReviewTargetType.MERCHANT_PRODUCT) {
            MerchantProductEntity product = requireProduct(task.getTargetId());
            product.setStatus(ContentStatus.REJECTED);
            product.setRejectReason(reason.trim());
        } else {
            MerchantEntity merchant = requireMerchant(task.getTargetId());
            merchant.setStatus(ContentStatus.REJECTED);
            merchant.setRejectReason(reason.trim());
        }
        finishReview(task, principal, ReviewStatus.REJECTED, reason.trim());
        auditLogService.record(principal.userId(), "review_reject", task.getTargetType().name(), task.getTargetId(), Map.of("reviewId", task.getId(), "reason", reason.trim()));
    }

    @Transactional
    public void close(SessionPrincipal principal, Long id, String reason) {
        ReviewTaskEntity task = requireReview(id);
        if (task.getTargetType() == ReviewTargetType.MERCHANT_PRODUCT) {
            MerchantProductEntity product = requireProduct(task.getTargetId());
            if (product.getStatus() != ContentStatus.PUBLISHED) {
                throw new BusinessException(ErrorCode.CONFLICT, "只有已发布产品可以下架");
            }
            product.setStatus(ContentStatus.CLOSED);
        } else {
            MerchantEntity merchant = requireMerchant(task.getTargetId());
            merchant.setStatus(ContentStatus.CLOSED);
        }
        finishReview(task, principal, ReviewStatus.CLOSED, reason);
        auditLogService.record(principal.userId(), "review_close", task.getTargetType().name(), task.getTargetId(), Map.of("reviewId", task.getId()));
    }

    @Transactional(readOnly = true)
    public PageResponse<MerchantSummaryResponse> merchants(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        Page<MerchantEntity> pageData = merchantRepository.findAll(PageRequest.of(safePage - 1, safePageSize, Sort.by("id")));
        List<MerchantSummaryResponse> content = pageData.getContent().stream().map(merchant -> new MerchantSummaryResponse(
                merchant.getId().toString(), merchant.getName(), merchant.getOwner(), merchant.getPhone(), merchant.getAddress(), merchant.getStatus().name().toLowerCase())).toList();
        return PageResponse.from(pageData, content);
    }

    @Transactional(readOnly = true)
    public List<AdminCategoryResponse> categories() {
        return categoryRepository.findAllByOrderByPriorityLevelAscSortOrderAsc().stream().map(this::toCategory).toList();
    }

    @Transactional
    public AdminCategoryResponse createCategory(AdminCategoryRequest request) {
        validateCategoryCreate(request);
        if (categoryRepository.findByCode(request.code()).isPresent()) {
            throw new BusinessException(ErrorCode.CONFLICT, "品类编码已存在");
        }
        ProductCategoryEntity category = new ProductCategoryEntity();
        category.setCode(request.code());
        category.setName(request.name().trim());
        category.setPriorityLevel(request.priorityLevel());
        category.setSortOrder(request.sort());
        category.setStatus(parseCategoryStatus(request.status()));
        return toCategory(categoryRepository.save(category));
    }

    @Transactional
    public AdminCategoryResponse updateCategory(Long id, AdminCategoryRequest request) {
        ProductCategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "品类不存在"));
        if (request.code() != null && !request.code().equals(category.getCode())) {
            if (categoryRepository.findByCode(request.code()).isPresent()) {
                throw new BusinessException(ErrorCode.CONFLICT, "品类编码已存在");
            }
            category.setCode(request.code());
        }
        if (request.name() != null) category.setName(request.name().trim());
        if (request.priorityLevel() != null) category.setPriorityLevel(request.priorityLevel());
        if (request.sort() != null) category.setSortOrder(request.sort());
        if (request.status() != null) category.setStatus(parseCategoryStatus(request.status()));
        return toCategory(category);
    }

    @Transactional(readOnly = true)
    public PageResponse<Map<String, Object>> auditLogs(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        var pageData = auditLogRepository.findAll(PageRequest.of(safePage - 1, safePageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<Map<String, Object>> content = pageData.getContent().stream().map(log -> Map.<String, Object>of(
                "id", log.getId().toString(),
                "actorUserId", log.getActorUserId().toString(),
                "action", log.getAction(),
                "targetType", log.getTargetType(),
                "targetId", log.getTargetId().toString(),
                "detailJson", log.getDetailJson() == null ? "{}" : log.getDetailJson(),
                "createdAt", log.getCreatedAt())).toList();
        return PageResponse.from(pageData, content);
    }

    private AdminReviewResponse toReview(ReviewTaskEntity task) {
        if (task.getTargetType() == ReviewTargetType.MERCHANT_PRODUCT) {
            MerchantProductEntity product = requireProduct(task.getTargetId());
            return new AdminReviewResponse(
                    task.getId().toString(), "merchant_product", product.getId().toString(), task.getMerchantId().toString(),
                    product.getTitle(), product.getMerchant().getName(), product.getCategory().getName(), product.getSummary(),
                    fileService.resolveUrl(product.getCoverFileId()), task.getStatus().name().toLowerCase(), task.getSubmittedAt(), task.getReviewComment());
        }
        MerchantEntity merchant = requireMerchant(task.getTargetId());
        return new AdminReviewResponse(
                task.getId().toString(), "merchant_profile", merchant.getId().toString(), merchant.getId().toString(),
                merchant.getName(), merchant.getOwner(), null, merchant.getIntro(), fileService.resolveUrl(merchant.getCoverFileId()),
                task.getStatus().name().toLowerCase(), task.getSubmittedAt(), task.getReviewComment());
    }

    private void finishReview(ReviewTaskEntity task, SessionPrincipal principal, ReviewStatus status, String comment) {
        task.setStatus(status);
        task.setReviewerUserId(principal.userId());
        task.setReviewComment(comment == null ? "" : comment.trim());
        task.setReviewedAt(Instant.now());
    }

    private ReviewTaskEntity requireReview(Long id) {
        return reviewTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "审核任务不存在"));
    }

    private ReviewTaskEntity requirePendingReview(Long id) {
        ReviewTaskEntity task = requireReview(id);
        if (task.getStatus() != ReviewStatus.PENDING) {
            throw new BusinessException(ErrorCode.CONFLICT, "审核任务已处理");
        }
        return task;
    }

    private MerchantProductEntity requireProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
    }

    private MerchantEntity requireMerchant(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商家不存在"));
    }

    private AdminCategoryResponse toCategory(ProductCategoryEntity category) {
        return new AdminCategoryResponse(
                category.getId().toString(), category.getCode(), category.getName(), category.getPriorityLevel(),
                category.getSortOrder(), category.getStatus().name().toLowerCase());
    }

    private void validateCategoryCreate(AdminCategoryRequest request) {
        if (request.code() == null || request.name() == null || request.name().isBlank()
                || request.priorityLevel() == null || request.sort() == null || request.status() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "品类编码、名称、重点等级、排序和状态均为必填项");
        }
    }

    private ReviewStatus parseReviewStatus(String status) {
        try {
            return ReviewStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "审核状态无效");
        }
    }

    private ReviewTargetType parseTargetType(String targetType) {
        try {
            return ReviewTargetType.valueOf(targetType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "审核类型无效");
        }
    }

    private CategoryStatus parseCategoryStatus(String status) {
        try {
            return CategoryStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "品类状态无效");
        }
    }
}
