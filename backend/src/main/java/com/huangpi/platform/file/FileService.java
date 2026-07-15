package com.huangpi.platform.file;

import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.config.AppProperties;
import com.huangpi.platform.domain.FileAssetEntity;
import com.huangpi.platform.domain.FileStatus;
import com.huangpi.platform.domain.UserRole;
import com.huangpi.platform.file.dto.FileResponse;
import com.huangpi.platform.repository.FileAssetRepository;
import com.huangpi.platform.security.SessionPrincipal;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp");

    private final FileAssetRepository fileAssetRepository;
    private final AppProperties properties;
    private Path storageDirectory;

    public FileService(FileAssetRepository fileAssetRepository, AppProperties properties) {
        this.fileAssetRepository = fileAssetRepository;
        this.properties = properties;
    }

    @PostConstruct
    void initializeStorage() throws IOException {
        storageDirectory = Path.of(properties.storage().directory()).toAbsolutePath().normalize();
        Files.createDirectories(storageDirectory);
    }

    @Transactional
    public FileResponse upload(MultipartFile file, String businessType, SessionPrincipal principal) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请选择图片文件");
        }
        String extension = ALLOWED_TYPES.get(file.getContentType());
        if (extension == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "仅支持 jpg、png、webp 图片");
        }
        String storageName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path target = storageDirectory.resolve(storageName).normalize();
        if (!target.startsWith(storageDirectory)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "非法文件路径");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "图片保存失败");
        }

        FileAssetEntity asset = new FileAssetEntity();
        asset.setStorageName(storageName);
        asset.setOriginalName(file.getOriginalFilename() == null ? storageName : file.getOriginalFilename());
        asset.setContentType(file.getContentType());
        asset.setSizeBytes(file.getSize());
        asset.setBusinessType(businessType == null || businessType.isBlank() ? "unknown" : businessType);
        asset.setUploaderUserId(principal.userId());
        asset.setStatus(FileStatus.ACTIVE);
        fileAssetRepository.save(asset);
        return toResponse(asset);
    }

    @Transactional(readOnly = true)
    public FileResponse get(Long id) {
        return toResponse(findActive(id));
    }

    @Transactional
    public void delete(Long id, SessionPrincipal principal) {
        FileAssetEntity asset = findActive(id);
        if (principal.role() != UserRole.ADMIN && !asset.getUploaderUserId().equals(principal.userId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        asset.setStatus(FileStatus.DELETED);
    }

    @Transactional(readOnly = true)
    public String resolveUrl(Long id) {
        if (id == null) {
            return "";
        }
        return fileAssetRepository.findById(id)
                .filter(asset -> asset.getStatus() == FileStatus.ACTIVE)
                .map(this::urlFor)
                .orElse("");
    }

    private FileAssetEntity findActive(Long id) {
        FileAssetEntity asset = fileAssetRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文件不存在"));
        if (asset.getStatus() != FileStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文件不存在");
        }
        return asset;
    }

    private FileResponse toResponse(FileAssetEntity asset) {
        return new FileResponse(asset.getId().toString(), urlFor(asset));
    }

    private String urlFor(FileAssetEntity asset) {
        return properties.storage().publicBaseUrl().replaceAll("/$", "") + "/uploads/" + asset.getStorageName();
    }
}
