package com.huangpi.platform.file;

import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.file.dto.FileResponse;
import com.huangpi.platform.security.SessionPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public ApiResponse<FileResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "merchant_product") String businessType,
            @AuthenticationPrincipal SessionPrincipal principal) {
        return ApiResponse.success(fileService.upload(file, businessType, principal));
    }

    @GetMapping("/{id}")
    public ApiResponse<FileResponse> get(@PathVariable Long id) {
        return ApiResponse.success(fileService.get(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, @AuthenticationPrincipal SessionPrincipal principal) {
        fileService.delete(id, principal);
        return ApiResponse.success();
    }
}
