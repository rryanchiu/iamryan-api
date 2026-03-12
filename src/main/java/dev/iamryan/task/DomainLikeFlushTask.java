package dev.iamryan.task;


import dev.iamryan.repository.DomainLikeRepository;
import dev.iamryan.entity.DomainLikeEntity;
import dev.iamryan.util.DomainLikeBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class DomainLikeFlushTask {

    private final DomainLikeBuffer domainLikeBuffer;
    private final DomainLikeRepository likeRepository;

    @Autowired
    public DomainLikeFlushTask(DomainLikeBuffer domainLikeBuffer, DomainLikeRepository likeRepository) {
        this.domainLikeBuffer = domainLikeBuffer;
        this.likeRepository = likeRepository;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void flushLikes() {
        Map<String, Long> snapshot = domainLikeBuffer.snapshotAndClear();

        for (Map.Entry<String, Long> entry : snapshot.entrySet()) {
            String domain = entry.getKey();
            long likeCount = entry.getValue();
            int updatedRows = likeRepository.incrementLikes(domain, likeCount);
            if (updatedRows == 0) {
                DomainLikeEntity entity = new DomainLikeEntity();
                entity.setDomain(domain);
                entity.setLikes(likeCount);
                entity.setCreateTime(LocalDateTime.now());
                likeRepository.save(entity);
            }
        }
    }
}
