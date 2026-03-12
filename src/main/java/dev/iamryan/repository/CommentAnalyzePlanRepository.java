package dev.iamryan.repository;

import dev.iamryan.entity.CommentAnalyzePlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface CommentAnalyzePlanRepository  extends JpaRepository<CommentAnalyzePlanEntity, Long> {
    @Modifying
    @Query("UPDATE CommentAnalyzePlanEntity c SET c.todayUsedCount = c.todayUsedCount + 1, c.updatedAt = :now WHERE c.deviceId = :deviceId AND c.date = :date")
    int incrementTodayUsedCount(@Param("deviceId") String deviceId,
                                @Param("date") LocalDate date,
                                @Param("now") LocalDateTime now);
    @Modifying
    @Query("UPDATE CommentAnalyzePlanEntity c SET c.todayUsedCount = c.todayUsedCount - 1, c.updatedAt = :now WHERE c.deviceId = :deviceId AND c.date = :date")
    int decrementTodayUsedCount(@Param("deviceId") String deviceId,
                                @Param("date") LocalDate date,
                                @Param("now") LocalDateTime now);

    CommentAnalyzePlanEntity findTopByDeviceIdAndDateOrderByCreatedAtDesc(String deviceId, LocalDate date);

    CommentAnalyzePlanEntity findTopByRecoveryCode(String recoveryCode);

}
