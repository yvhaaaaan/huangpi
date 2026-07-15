package com.huangpi.platform.repository;

import com.huangpi.platform.domain.AccountUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountUserRepository extends JpaRepository<AccountUserEntity, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<AccountUserEntity> findByUsername(String username);
}
