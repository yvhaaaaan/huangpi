package com.huangpi.platform.merchant;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.MerchantEntity;
import com.huangpi.platform.domain.MerchantProductEntity;
import com.huangpi.platform.domain.ReviewStatus;
import com.huangpi.platform.domain.ReviewTargetType;
import com.huangpi.platform.domain.ReviewTaskEntity;
import com.huangpi.platform.file.FileService;
import com.huangpi.platform.merchant.dto.MerchantDashboardResponse;
import com.huangpi.platform.merchant.dto.MerchantProductResponse;
import com.huangpi.platform.merchant.dto.MerchantProductUpsertRequest;
import com.huangpi.platform.merchant.dto.MerchantProfileResponse;
import com.huangpi.platform.merchant.dto.MerchantProfileUpdateRequest;
import com.huangpi.platform.product.CategoryService;
import com.huangpi.platform.repository.MerchantProductRepository;
import com.huangpi.platform.repository.MerchantRepository;
import com.huangpi.platform.repository.ReviewTaskRepository;
import com.huangpi.platform.security.SessionPrincipal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantProductRepository productRepository;
    private final ReviewTaskRepository reviewTaskRepository;
    private final CategoryService categoryService;
    private final FileService fileService;

    public MerchantService(
            MerchantRepository merchantRepository,
            MerchantProductRepository productRepository,
            ReviewTaskRepository reviewTaskRepository,
            CategoryService categoryService,
            FileService fileService) {
        this.merchantRepository = merchantRepository;
        this.productRepository = productRepository;
        this.reviewTaskRepository = reviewTaskRepository;
        this.categoryService = categoryService;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public MerchantDashboardResponse dashboard(SessionPrincipal principal) {
        Long merchantId = requireMerchantId(principal);
        var stats = new MerchantDashboardResponse.Stats(
                productRepository.countByMerchantIdAndStatus(merchantId, ContentStatus.PUBLISHED),
                productRepository.countByMerchantIdAndStatus(merchantId, ContentStatus.PENDING),
                productRepository.countByMerchantIdAndStatus(merchantId, ContentStatus.DRAFT));

        List<MerchantDashboardResponse.Message> messages = new ArrayList<>();
        List<MerchantDashboardResponse.Todo> todos = new ArrayList<>();
        for (MerchantProductEntity product : productRepository.findByMerchantIdOrderByUpdatedAtDesc(merchantId)) {
            if (messages.size() < 10 && product.getStatus() == ContentStatus.REJECTED) {
                messages.add(new MerchantDashboardResponse.Message(
                        "product-" + product.getId(), "审核", "内容审核未通过",
                        "“" + product.getTitle() + "”需要修改：" + valueOrDefault(product.getRejectReason(), "请查看审核意见"),
                        product.getUpdatedAt()));
            } else if (messages.size() < 10 && product.getStatus() == ContentStatus.PUBLISHED) {
                messages.add(new MerchantDashboardResponse.Message(
                        "product-" + product.getId(), "发布", "内容已成功发布",
                        "“" + product.getTitle() + "”已在用户端展示", product.getUpdatedAt()));
            }

            if (product.getStatus() == ContentStatus.REJECTED) {
                todos.add(new MerchantDashboardResponse.Todo(
                        "product-" + product.getId(), "modify_product", "修改“" + product.getTitle() + "”",
                        valueOrDefault(product.getRejectReason(), "请根据审核意见修改后重新提交"),
                        product.getId().toString(), "urgent"));
            } else if (product.getStatus() == ContentStatus.PENDING) {
                todos.add(new MerchantDashboardResponse.Todo(
                        "product-" + product.getId(), "wait_review", "等待“" + product.getTitle() + "”审核",
                        "政府管理员处理后会更新审核状态", product.getId().toString(), "normal"));
            }
        }
        return new MerchantDashboardResponse(stats, messages, todos);
    }

    @Transactional(readOnly = true)
    public MerchantProfileResponse profile(SessionPrincipal principal) {
        return toProfile(requireMerchant(principal));
    }

    @Transactional
    public MerchantProfileResponse updateProfile(SessionPrincipal principal, MerchantProfileUpdateRequest request) {
        MerchantEntity merchant = requireMerchant(principal);
        if (merchant.getStatus() == ContentStatus.PENDING) {
            throw new BusinessException(ErrorCode.CONFLICT, "商家资料正在审核中");
        }
        if (request.name() != null) merchant.setName(request.name().trim());
        if (request.owner() != null) merchant.setOwner(request.owner().trim());
        if (request.phone() != null) merchant.setPhone(request.phone().trim());
        if (request.address() != null) merchant.setAddress(request.address().trim());
        if (request.businessHours() != null) merchant.setBusinessHours(request.businessHours().trim());
        if (request.intro() != null) merchant.setIntro(request.intro().trim());
        if (request.coverFileId() != null) merchant.setCoverFileId(parseNullableId(request.coverFileId(), "coverFileId"));
        merchant.setStatus(ContentStatus.DRAFT);
        merchant.setRejectReason(null);
        return toProfile(merchant);
    }

    @Transactional
    public void submitProfile(SessionPrincipal principal) {
        MerchantEntity merchant = requireMerchant(principal);
        if (isBlank(merchant.getName()) || isBlank(merchant.getPhone()) || isBlank(merchant.getAddress())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "商家名称、联系电话和地址为必填项");
        }
        createReviewTask(principal, merchant.getId(), ReviewTargetType.MERCHANT_PROFILE);
        merchant.setStatus(ContentStatus.PENDING);
        merchant.setRejectReason(null);
    }

    @Transactional(readOnly = true)
    public PageResponse<MerchantProductResponse> products(SessionPrincipal principal, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        Page<MerchantProductEntity> pageData = productRepository.findByMerchantId(
                requireMerchantId(principal),
                PageRequest.of(safePage - 1, safePageSize, Sort.by(Sort.Direction.DESC, "updatedAt")));
        return PageResponse.from(pageData, pageData.getContent().stream().map(this::toProduct).toList());
    }

    @Transactional(readOnly = true)
    public MerchantProductResponse product(SessionPrincipal principal, Long id) {
        return toProduct(requireOwnedProduct(principal, id));
    }

    @Transactional
    public MerchantProductResponse createProduct(SessionPrincipal principal, MerchantProductUpsertRequest request) {
        MerchantEntity merchant = requireMerchant(principal);
        if (isBlank(request.categoryId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请选择产品品类");
        }
        MerchantProductEntity product = new MerchantProductEntity();
        product.setMerchant(merchant);
        product.setCategory(categoryService.requireEnabled(parseId(request.categoryId(), "categoryId")));
        product.setTitle(isBlank(request.title()) ? "未命名产品" : request.title().trim());
        product.setSummary(request.summary() == null ? "" : request.summary().trim());
        product.setStatus(ContentStatus.DRAFT);
        applyProductFields(product, request, false);
        return toProduct(productRepository.save(product));
    }

    @Transactional
    public MerchantProductResponse updateProduct(SessionPrincipal principal, Long id, MerchantProductUpsertRequest request) {
        MerchantProductEntity product = requireOwnedProduct(principal, id);
        if (product.getStatus() == ContentStatus.PENDING) {
            throw new BusinessException(ErrorCode.CONFLICT, "产品正在审核中，暂不能修改");
        }
        if (product.getStatus() == ContentStatus.CLOSED) {
            throw new BusinessException(ErrorCode.CONFLICT, "已下架产品不能继续修改");
        }
        applyProductFields(product, request, true);
        product.setStatus(ContentStatus.DRAFT);
        product.setRejectReason(null);
        product.setPublishedAt(null);
        return toProduct(product);
    }

    @Transactional
    public void submitProduct(SessionPrincipal principal, Long id) {
        MerchantProductEntity product = requireOwnedProduct(principal, id);
        if (product.getStatus() == ContentStatus.PENDING || product.getStatus() == ContentStatus.CLOSED) {
            throw new BusinessException(ErrorCode.CONFLICT);
        }
        validateForSubmission(product);
        createReviewTask(principal, product.getId(), ReviewTargetType.MERCHANT_PRODUCT);
        product.setStatus(ContentStatus.PENDING);
        product.setRejectReason(null);
    }

    @Transactional
    public void closeProduct(SessionPrincipal principal, Long id) {
        MerchantProductEntity product = requireOwnedProduct(principal, id);
        if (product.getStatus() != ContentStatus.PUBLISHED) {
            throw new BusinessException(ErrorCode.CONFLICT, "只有已发布产品可以下架");
        }
        product.setStatus(ContentStatus.CLOSED);
    }

    private void applyProductFields(MerchantProductEntity product, MerchantProductUpsertRequest request, boolean partial) {
        if (request.categoryId() != null) {
            product.setCategory(categoryService.requireEnabled(parseId(request.categoryId(), "categoryId")));
        }
        if (request.title() != null) product.setTitle(request.title().trim());
        if (request.summary() != null) product.setSummary(request.summary().trim());
        if (request.content() != null) product.setContent(request.content().trim());
        if (request.coverFileId() != null) product.setCoverFileId(parseNullableId(request.coverFileId(), "coverFileId"));
        if (request.imageFileIds() != null) {
            product.setImageFileIds(request.imageFileIds().stream().map(value -> parseId(value, "imageFileIds")).toList());
        }
        if (request.address() != null) product.setAddress(request.address().trim());
        if (request.contactPhone() != null) product.setContactPhone(request.contactPhone().trim());
        if (request.businessHours() != null) product.setBusinessHours(request.businessHours().trim());
        if (request.latitude() != null || !partial) product.setLatitude(request.latitude());
        if (request.longitude() != null || !partial) product.setLongitude(request.longitude());
    }

    private void validateForSubmission(MerchantProductEntity product) {
        if (isBlank(product.getTitle()) || "未命名产品".equals(product.getTitle())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请填写产品名称");
        }
        if (isBlank(product.getSummary())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请填写产品简介");
        }
        if (product.getCoverFileId() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请上传产品封面图");
        }
        categoryService.requireEnabled(product.getCategory().getId());
    }

    private void createReviewTask(SessionPrincipal principal, Long targetId, ReviewTargetType targetType) {
        if (reviewTaskRepository.findByTargetTypeAndTargetIdAndStatus(targetType, targetId, ReviewStatus.PENDING).isPresent()) {
            throw new BusinessException(ErrorCode.CONFLICT, "已有待处理的审核任务");
        }
        ReviewTaskEntity task = new ReviewTaskEntity();
        task.setTargetType(targetType);
        task.setTargetId(targetId);
        task.setMerchantId(requireMerchantId(principal));
        task.setSubmitterUserId(principal.userId());
        task.setStatus(ReviewStatus.PENDING);
        task.setSubmittedAt(Instant.now());
        reviewTaskRepository.save(task);
    }

    private MerchantEntity requireMerchant(SessionPrincipal principal) {
        return merchantRepository.findById(requireMerchantId(principal))
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN, "商家资料不存在"));
    }

    private Long requireMerchantId(SessionPrincipal principal) {
        if (principal.merchantId() == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号未绑定商家");
        }
        return principal.merchantId();
    }

    private MerchantProductEntity requireOwnedProduct(SessionPrincipal principal, Long id) {
        return productRepository.findByIdAndMerchantId(id, requireMerchantId(principal))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
    }

    private MerchantProfileResponse toProfile(MerchantEntity merchant) {
        return new MerchantProfileResponse(
                merchant.getId().toString(), merchant.getName(), merchant.getOwner(), merchant.getPhone(),
                merchant.getAddress(), merchant.getBusinessHours(), merchant.getIntro(),
                merchant.getCoverFileId() == null ? null : merchant.getCoverFileId().toString(),
                fileService.resolveUrl(merchant.getCoverFileId()), merchant.getStatus().name().toLowerCase(), merchant.getRejectReason());
    }

    private MerchantProductResponse toProduct(MerchantProductEntity product) {
        return new MerchantProductResponse(
                product.getId().toString(), product.getTitle(), product.getCategory().getId().toString(), product.getCategory().getName(),
                product.getSummary(), product.getContent(), product.getCoverFileId() == null ? null : product.getCoverFileId().toString(),
                fileService.resolveUrl(product.getCoverFileId()), product.getImageFileIds().stream().map(String::valueOf).toList(),
                product.getAddress(), product.getContactPhone(), product.getBusinessHours(), product.getStatus().name().toLowerCase(),
                statusLabel(product.getStatus()), product.getRejectReason(), product.getViewCount(), product.getUpdatedAt());
    }

    private String statusLabel(ContentStatus status) {
        return switch (status) {
            case DRAFT -> "草稿";
            case PENDING -> "待审核";
            case PUBLISHED -> "已发布";
            case REJECTED -> "已驳回";
            case CLOSED -> "已下架";
        };
    }

    private Long parseId(String value, String field) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, field + " 格式错误");
        }
    }

    private Long parseNullableId(String value, String field) {
        return isBlank(value) ? null : parseId(value, field);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String valueOrDefault(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }
}
