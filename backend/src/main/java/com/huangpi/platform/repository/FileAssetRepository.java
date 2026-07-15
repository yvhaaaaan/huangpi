package com.huangpi.platform.repository;

import com.huangpi.platform.domain.FileAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAssetRepository extends JpaRepository<FileAssetEntity, Long> {
}
