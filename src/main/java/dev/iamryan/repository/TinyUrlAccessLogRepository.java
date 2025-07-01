package me.rryan.tinyurl.repository;

import me.rryan.tinyurl.entity.TinyUrlAccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinyUrlAccessLogRepository extends JpaRepository<TinyUrlAccessLogEntity, Long> {
}
