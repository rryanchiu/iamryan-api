package dev.iamryan.repository;

import dev.iamryan.entity.TinyUrlAccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinyUrlAccessLogRepository extends JpaRepository<TinyUrlAccessLogEntity, Long> {
}
