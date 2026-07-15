package com.huangpi.platform.product;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.common.PageResponse;
import com.huangpi.platform.domain.ContentStatus;
import com.huangpi.platform.domain.MerchantProductEntity;
import com.huangpi.platform.file.FileService;
import com.huangpi.platform.product.dto.ProductDetailResponse;
import com.huangpi.platform.product.dto.ProductSummaryResponse;
import com.huangpi.platform.repository.MerchantProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final MerchantProductRepository productRepository;
    private final FileService fileService;

    public ProductService(MerchantProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> listPublished(String categoryCode, String keyword, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        Specification<MerchantProductEntity> specification = (root, query, builder) -> builder.equal(root.get("status"), ContentStatus.PUBLISHED);
        if (categoryCode != null && !categoryCode.isBlank()) {
            specification = specification.and((root, query, builder) -> builder.equal(root.join("category").get("code"), categoryCode));
        }
        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.trim().toLowerCase() + "%";
            specification = specification.and((root, query, builder) -> builder.or(
                    builder.like(builder.lower(root.get("title")), like),
                    builder.like(builder.lower(root.get("summary")), like)));
        }
        Page<MerchantProductEntity> pageData = productRepository.findAll(
                specification,
                PageRequest.of(safePage - 1, safePageSize, Sort.by(Sort.Direction.DESC, "publishedAt")));
        return PageResponse.from(pageData, pageData.getContent().stream().map(this::toSummary).toList());
    }

    @Transactional
    public ProductDetailResponse getPublished(Long id) {
        MerchantProductEntity product = productRepository.findById(id)
                .filter(item -> item.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "产品不存在"));
        product.setViewCount(product.getViewCount() + 1);
        return toDetail(product);
    }

    public ProductSummaryResponse toSummary(MerchantProductEntity product) {
        return new ProductSummaryResponse(
                product.getId().toString(),
                product.getCategory().getId().toString(),
                product.getCategory().getCode(),
                product.getCategory().getName(),
                product.getTitle(),
                product.getSummary(),
                fileService.resolveUrl(product.getCoverFileId()),
                product.getMerchant().getName(),
                product.getAddress(),
                product.getStatus().name().toLowerCase());
    }

    public ProductDetailResponse toDetail(MerchantProductEntity product) {
        List<String> imageUrls = new ArrayList<>();
        for (Long fileId : product.getImageFileIds()) {
            String url = fileService.resolveUrl(fileId);
            if (!url.isBlank()) {
                imageUrls.add(url);
            }
        }
        return new ProductDetailResponse(
                product.getId().toString(),
                product.getCategory().getId().toString(),
                product.getCategory().getCode(),
                product.getCategory().getName(),
                product.getTitle(),
                product.getSummary(),
                product.getContent(),
                fileService.resolveUrl(product.getCoverFileId()),
                imageUrls,
                product.getMerchant().getName(),
                product.getAddress(),
                product.getContactPhone(),
                product.getBusinessHours(),
                product.getLatitude(),
                product.getLongitude(),
                product.getStatus().name().toLowerCase(),
                product.getPublishedAt());
    }
}
