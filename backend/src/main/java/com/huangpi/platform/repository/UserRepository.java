package com.huangpi.platform.repository;

import com.huangpi.platform.domain.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByOpenid(String openid);
}
