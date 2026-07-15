package com.huangpi.platform.repository;

import com.huangpi.platform.domain.ActivitySignupEntity;
import com.huangpi.platform.domain.ActivitySignupStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivitySignupRepository extends JpaRepository<ActivitySignupEntity, Long> {
    boolean existsByActivityIdAndUserId(Long activityId, Long userId);
    List<ActivitySignupEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("select coalesce(sum(signup.peopleCount), 0) from ActivitySignupEntity signup "
            + "where signup.activity.id = :activityId and signup.status = :status")
    long sumPeopleCount(@Param("activityId") Long activityId, @Param("status") ActivitySignupStatus status);
}
